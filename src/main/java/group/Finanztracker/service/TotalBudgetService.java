package group.Finanztracker.service;

import group.Finanztracker.dto.RolloverSettingsForm;
import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.TotalBudgetMapper;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.repository.security.AppUserRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TotalBudgetService {

    private final TotalBudgetRepository totalBudgetRepository;
    private final TotalBudgetMapper totalBudgetMapper;
    private final CurrentUserService currentUserService;
    private final AppUserRepository appUserRepository;

    public Optional<TotalBudgetResponse> getCurrentBudget() {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(totalBudgetMapper::toResponse);
    }

    public Optional<RolloverSettingsForm> getRolloverSettings() {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(b -> {
                    String startMonth = b.getRolloverStartMonth() != null
                            ? YearMonth.from(b.getRolloverStartMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM"))
                            : "";
                    return RolloverSettingsForm.builder()
                            .rolloverEnabled(b.isRolloverEnabled())
                            .rolloverStartMonth(startMonth)
                            .build();
                });
    }

    @Transactional
    public TotalBudgetResponse saveOrUpdate(TotalBudgetRequest request) {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(existing -> update(existing.getId(), request))
                .orElseGet(() -> create(request));
    }

    @Transactional
    public void saveRolloverSettings(RolloverSettingsForm form) {
        TotalBudget budget = totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kein Gesamtbudget konfiguriert"));
        budget.setRolloverEnabled(form.isRolloverEnabled());
        if (form.isRolloverEnabled()
                && form.getRolloverStartMonth() != null
                && !form.getRolloverStartMonth().isBlank()) {
            LocalDate startDate = YearMonth.parse(form.getRolloverStartMonth()).atDay(1);
            budget.setRolloverStartMonth(startDate);
        }
        totalBudgetRepository.save(budget);
    }

    private TotalBudgetResponse create(TotalBudgetRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        TotalBudget entity = totalBudgetMapper.toEntity(
                request,
                appUserRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"))
        );
        entity = totalBudgetRepository.save(entity);
        return totalBudgetMapper.toResponse(entity);
    }

    private TotalBudgetResponse update(Long id, TotalBudgetRequest request) {
        TotalBudget entity = totalBudgetRepository.findByIdAndUser_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Gesamtbudget mit ID " + id + " nicht gefunden"));
        totalBudgetMapper.updateEntity(entity, request);
        entity = totalBudgetRepository.save(entity);
        return totalBudgetMapper.toResponse(entity);
    }
}
