package group.Finanztracker.service;

import group.Finanztracker.dto.BudgetSettingsViewModel;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetSettingsQueryService {

    private final TotalBudgetRepository totalBudgetRepository;
    private final CategoryBudgetService categoryBudgetService;
    private final CurrentUserService currentUserService;

    public BudgetSettingsViewModel getViewModel() {
        Long userId = currentUserService.getCurrentUserId();
        Optional<TotalBudget> totalBudget = totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(userId);
        BigDecimal totalMonthlyLimit = totalBudget.map(TotalBudget::getTotalMonthlyLimit).orElse(BigDecimal.ZERO);
        BigDecimal configuredCategoryBudgetSum = categoryBudgetService.getConfiguredCategoryBudgetSum();

        return BudgetSettingsViewModel.builder()
                .totalBudgetId(totalBudget.map(TotalBudget::getId).orElse(null))
                .totalMonthlyLimit(totalMonthlyLimit)
                .configuredCategoryBudgetSum(configuredCategoryBudgetSum)
                .categoryBudgetSumExceedsTotalBudget(totalMonthlyLimit.compareTo(BigDecimal.ZERO) > 0
                        && configuredCategoryBudgetSum.compareTo(totalMonthlyLimit) > 0)
                .categoryBudgets(categoryBudgetService.getAll())
                .build();
    }
}
