package group.Finanztracker.service;

import group.Finanztracker.dto.SubscriptionRequest;
import group.Finanztracker.dto.SubscriptionResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.Subscription;
import group.Finanztracker.entity.Transaction;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.SubscriptionMapper;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.SubscriptionRepository;
import group.Finanztracker.repository.TransactionRepository;
import group.Finanztracker.repository.security.AppUserRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final CurrentUserService currentUserService;
    private final AppUserRepository appUserRepository;

    public List<SubscriptionResponse> findAll() {
        return subscriptionRepository.findAllByUser_IdOrderByActiveDescTitleAsc(currentUserService.getCurrentUserId()).stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    public SubscriptionResponse findById(Long id) {
        return subscriptionMapper.toResponse(getSubscriptionOrThrow(id));
    }

    @Transactional
    public SubscriptionResponse create(SubscriptionRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Category category = getCategoryOrThrow(request.getCategoryId());
        Subscription subscription = subscriptionMapper.toEntity(
                request,
                category,
                appUserRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"))
        );
        Subscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toResponse(saved);
    }

    @Transactional
    public SubscriptionResponse update(Long id, SubscriptionRequest request) {
        Subscription subscription = getSubscriptionOrThrow(id);
        Category category = getCategoryOrThrow(request.getCategoryId());
        subscriptionMapper.updateEntity(subscription, request, category);
        Subscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        subscriptionRepository.delete(getSubscriptionOrThrow(id));
    }

    @Transactional
    public void synchronizeTransactionsUpTo(YearMonth targetMonth) {
        Long userId = currentUserService.getCurrentUserId();
        for (Subscription subscription : subscriptionRepository.findAllByUser_IdAndActiveTrueOrderByIdAsc(userId)) {
            generateMissingTransactions(subscription, targetMonth);
        }
    }

    // Erzeugt fuer ein Abo alle noch fehlenden Transaktionen vom Startmonat bis zum letzten relevanten Monat.
    // Dabei wird monatsweise geprueft, ob das Abo in diesem Monat faellig ist und ob bereits eine Abo-Buchung existiert.
    private void generateMissingTransactions(Subscription subscription, YearMonth targetMonth) {
        YearMonth currentMonth = YearMonth.from(subscription.getStartDate());
        YearMonth lastRelevantMonth = getLastRelevantMonth(subscription, targetMonth);
        if (currentMonth.isAfter(lastRelevantMonth)) {
            return;
        }

        while (!currentMonth.isAfter(lastRelevantMonth)) {
            if (isDueInMonth(subscription, currentMonth)) {
                LocalDate bookingDate = resolveBookingDate(subscription, currentMonth);
                if (isWithinSubscriptionRuntime(subscription, bookingDate)
                        && !transactionRepository.existsBySubscription_IdAndDateBetween(
                        subscription.getId(),
                        currentMonth.atDay(1),
                        currentMonth.atEndOfMonth())) {
                    transactionRepository.save(Transaction.builder()
                            .title(subscription.getTitle())
                            .amount(subscription.getAmount())
                            .type(subscription.getType())
                            .category(subscription.getCategory())
                            .date(bookingDate)
                            .note(subscription.getNote())
                            .subscription(subscription)
                            .build());
                }
            }
            currentMonth = currentMonth.plusMonths(1);
        }
    }

    // Bestimmt den letzten Monat, der fuer die Synchronisierung ueberhaupt betrachtet werden darf.
    // Falls ein Enddatum gesetzt ist, wird spaetestens bis zu dessen Monat gearbeitet, sonst bis zum Zielmonat.
    private YearMonth getLastRelevantMonth(Subscription subscription, YearMonth targetMonth) {
        if (subscription.getEndDate() == null) {
            return targetMonth;
        }
        YearMonth endMonth = YearMonth.from(subscription.getEndDate());
        return endMonth.isBefore(targetMonth) ? endMonth : targetMonth;
    }

    // Prueft, ob das Abo im uebergebenen Monat gemaess seines Intervalls faellig ist.
    // Grundlage ist die Anzahl der Monate seit dem Startmonat, geteilt durch den Monatsabstand des Intervalls.
    private boolean isDueInMonth(Subscription subscription, YearMonth month) {
        long monthsBetween = ChronoUnit.MONTHS.between(
                YearMonth.from(subscription.getStartDate()),
                month
        );
        return monthsBetween >= 0 && monthsBetween % subscription.getInterval().getMonthsStep() == 0;
    }

    // Leitet aus einem faelligen Monat das konkrete Buchungsdatum ab.
    // Der Start-Tag des Abos wird beibehalten; existiert er im Zielmonat nicht, wird auf den letzten Monatstag gekuerzt.
    private LocalDate resolveBookingDate(Subscription subscription, YearMonth month) {
        int dayOfMonth = Math.min(subscription.getStartDate().getDayOfMonth(), month.lengthOfMonth());
        return month.atDay(dayOfMonth);
    }

    // Prueft, ob das konkret berechnete Buchungsdatum noch innerhalb der Laufzeit des Abos liegt.
    // Das ist wichtig, weil ein faelliger Monat trotzdem zu einem Datum nach dem Enddatum fuehren kann.
    private boolean isWithinSubscriptionRuntime(Subscription subscription, LocalDate bookingDate) {
        return !bookingDate.isBefore(subscription.getStartDate())
                && (subscription.getEndDate() == null || !bookingDate.isAfter(subscription.getEndDate()));
    }

    private Subscription getSubscriptionOrThrow(Long id) {
        return subscriptionRepository.findByIdAndUser_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Abonnement mit ID " + id + " nicht gefunden"));
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findByIdAndUser_Id(categoryId, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + categoryId + " nicht gefunden"));
    }
}
