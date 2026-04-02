package group.Finanztracker.service;

import group.Finanztracker.dto.BudgetSettingsViewModel;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetSettingsQueryService {

    private final TotalBudgetRepository totalBudgetRepository;
    private final CategoryBudgetService categoryBudgetService;
    private final CurrentUserService currentUserService;

    public BudgetSettingsViewModel getViewModel() {
        Long userId = currentUserService.getCurrentUserId();
        BigDecimal totalBudget = totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(userId)
                .map(total -> total.getTotalMonthlyLimit())
                .orElse(BigDecimal.ZERO);
        BigDecimal configuredCategoryBudgetSum = categoryBudgetService.getConfiguredCategoryBudgetSum();

        return BudgetSettingsViewModel.builder()
                .totalBudgetId(totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(userId).map(total -> total.getId()).orElse(null))
                .totalMonthlyLimit(totalBudget)
                .configuredCategoryBudgetSum(configuredCategoryBudgetSum)
                .categoryBudgetSumExceedsTotalBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0
                        && configuredCategoryBudgetSum.compareTo(totalBudget) > 0)
                .categoryBudgets(categoryBudgetService.getAll())
                .build();
    }
}
