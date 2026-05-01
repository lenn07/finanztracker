package group.Finanztracker.service;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.CategoryBudgetMapper;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryBudgetService {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final CategoryBudgetRepository categoryBudgetRepository;
    private final CategoryRepository categoryRepository;
    private final TotalBudgetRepository totalBudgetRepository;
    private final CategoryBudgetMapper categoryBudgetMapper;
    private final CurrentUserService currentUserService;

    public List<CategoryBudgetResponse> getAll() {
        Long userId = currentUserService.getCurrentUserId();
        BigDecimal totalLimit = currentTotalLimit(userId);
        return categoryBudgetRepository.findAllByCategory_User_IdOrderByCategory_NameAsc(userId).stream()
                .map(entity -> categoryBudgetMapper.toResponse(entity, totalLimit))
                .toList();
    }

    public CategoryBudgetResponse getById(Long id) {
        Long userId = currentUserService.getCurrentUserId();
        CategoryBudget entity = categoryBudgetRepository.findByIdAndCategory_User_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kategoriebudget mit ID " + id + " nicht gefunden"));
        return categoryBudgetMapper.toResponse(entity, currentTotalLimit(userId));
    }

    @Transactional
    public CategoryBudgetResponse create(CategoryBudgetRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        requireTotalBudget(userId);
        Category category = categoryRepository.findByIdAndUser_Id(request.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + request.getCategoryId() + " nicht gefunden"));
        if (categoryBudgetRepository.findByCategoryAndCategory_User_Id(category, userId).isPresent()) {
            throw new IllegalStateException("Für diese Kategorie existiert bereits ein Budget.");
        }
        ensurePercentageSumWithinLimit(userId, null, request.getPercentage());
        CategoryBudget entity = categoryBudgetMapper.toEntity(request, category);
        entity = categoryBudgetRepository.save(entity);
        return categoryBudgetMapper.toResponse(entity, currentTotalLimit(userId));
    }

    @Transactional
    public CategoryBudgetResponse update(Long id, CategoryBudgetRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        requireTotalBudget(userId);
        CategoryBudget entity = categoryBudgetRepository.findByIdAndCategory_User_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kategoriebudget mit ID " + id + " nicht gefunden"));
        Category category = categoryRepository.findByIdAndUser_Id(request.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + request.getCategoryId() + " nicht gefunden"));
        categoryBudgetRepository.findByCategoryAndCategory_User_Id(category, userId)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("Für diese Kategorie existiert bereits ein Budget.");
                });
        ensurePercentageSumWithinLimit(userId, id, request.getPercentage());
        categoryBudgetMapper.updateEntity(entity, request, category);
        entity = categoryBudgetRepository.save(entity);
        return categoryBudgetMapper.toResponse(entity, currentTotalLimit(userId));
    }

    @Transactional
    public void delete(Long id) {
        CategoryBudget entity = categoryBudgetRepository.findByIdAndCategory_User_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategoriebudget mit ID " + id + " nicht gefunden"));
        categoryBudgetRepository.delete(entity);
    }

    public BigDecimal getConfiguredPercentageSum() {
        return categoryBudgetRepository.findAllByCategory_User_Id(currentUserService.getCurrentUserId()).stream()
                .map(CategoryBudget::getPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean hasUsableTotalBudget() {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(TotalBudget::getTotalMonthlyLimit)
                .filter(limit -> limit.signum() > 0)
                .isPresent();
    }

    private BigDecimal currentTotalLimit(Long userId) {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(userId)
                .map(TotalBudget::getTotalMonthlyLimit)
                .orElse(BigDecimal.ZERO);
    }

    private void requireTotalBudget(Long userId) {
        BigDecimal limit = currentTotalLimit(userId);
        if (limit.signum() <= 0) {
            throw new IllegalStateException("Bitte zuerst ein Gesamtbudget anlegen, bevor Kategorie-Budgets hinzugefügt werden können.");
        }
    }

    private void ensurePercentageSumWithinLimit(Long userId, Long ignoreBudgetId, BigDecimal newPercentage) {
        BigDecimal currentSum = categoryBudgetRepository.findAllByCategory_User_Id(userId).stream()
                .filter(b -> ignoreBudgetId == null || !b.getId().equals(ignoreBudgetId))
                .map(CategoryBudget::getPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (currentSum.add(newPercentage).compareTo(HUNDRED) > 0) {
            BigDecimal remaining = HUNDRED.subtract(currentSum).max(BigDecimal.ZERO);
            throw new IllegalStateException("Summe aller Kategorie-Prozente darf 100 % nicht überschreiten. Verfügbar: "
                    + remaining.stripTrailingZeros().toPlainString() + " %.");
        }
    }
}
