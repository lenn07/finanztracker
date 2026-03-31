package group.Finanztracker.service;

import group.Finanztracker.dto.BudgetSettingsViewModel;
import group.Finanztracker.repository.TotalBudgetRepository;
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

    public BudgetSettingsViewModel getViewModel() {
        BigDecimal totalBudget = totalBudgetRepository.findFirstByOrderByIdAsc()
                .map(total -> total.getTotalMonthlyLimit())
                .orElse(BigDecimal.ZERO);
        BigDecimal configuredCategoryBudgetSum = categoryBudgetService.getConfiguredCategoryBudgetSum();

        return BudgetSettingsViewModel.builder()
                .totalBudgetId(totalBudgetRepository.findFirstByOrderByIdAsc().map(total -> total.getId()).orElse(null))
                .totalMonthlyLimit(totalBudget)
                .configuredCategoryBudgetSum(configuredCategoryBudgetSum)
                .categoryBudgetSumExceedsTotalBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0
                        && configuredCategoryBudgetSum.compareTo(totalBudget) > 0)
                .categoryBudgets(categoryBudgetService.getAll())
                .build();
    }
}
