package group.Finanztracker.service;

import group.Finanztracker.dto.BudgetSettingsViewModel;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.mapper.CategoryBudgetMapper;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetSettingsQueryService {

    private final TotalBudgetRepository totalBudgetRepository;
    private final CategoryBudgetService categoryBudgetService;
    private final CategoryBudgetMapper categoryBudgetMapper;
    private final CurrentUserService currentUserService;

    public BudgetSettingsViewModel getViewModel() {
        Long userId = currentUserService.getCurrentUserId();
        Optional<TotalBudget> totalBudget = totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(userId);
        BigDecimal totalMonthlyLimit = totalBudget.map(TotalBudget::getTotalMonthlyLimit).orElse(BigDecimal.ZERO);
        BigDecimal configuredPercentageSum = categoryBudgetService.getConfiguredPercentageSum();
        List<CategoryBudgetResponse> categoryBudgets = categoryBudgetService.getAll();
        BigDecimal configuredMonthlyLimitSum = categoryBudgetMapper
                .calculateMonthlyLimit(configuredPercentageSum, totalMonthlyLimit);

        return BudgetSettingsViewModel.builder()
                .totalBudgetId(totalBudget.map(TotalBudget::getId).orElse(null))
                .totalMonthlyLimit(totalMonthlyLimit)
                .configuredPercentageSum(configuredPercentageSum.setScale(2, RoundingMode.HALF_UP))
                .configuredMonthlyLimitSum(configuredMonthlyLimitSum)
                .totalBudgetConfigured(totalMonthlyLimit.signum() > 0)
                .categoryBudgets(categoryBudgets)
                .build();
    }
}
