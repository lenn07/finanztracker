package group.Finanztracker.service;

import group.Finanztracker.dto.MonthlyCategorySummaryResponse;
import group.Finanztracker.dto.MonthlyOverviewResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonthlyOverviewService {

    private final CategoryRepository categoryRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final TransactionService transactionService;
    private final TotalBudgetService totalBudgetService;
    private final Clock clock;
    private final CurrentUserService currentUserService;

    public MonthlyOverviewResponse getCurrentMonthOverview() {
        return getOverview(YearMonth.now(clock));
    }

    public MonthlyOverviewResponse getOverview(YearMonth month) {
        Long userId = currentUserService.getCurrentUserId();
        BigDecimal totalBudget = totalBudgetService.getCurrentBudgetLimit();
        BigDecimal totalSpent = transactionService.sumExpensesForMonth(month);
        BigDecimal totalIncome = transactionService.sumIncomeForMonth(month);
        BigDecimal configuredCategoryBudgetSum = getConfiguredCategoryBudgetSum(userId);

        Map<Long, CategoryBudget> budgetsByCategoryId = new HashMap<>();
        for (CategoryBudget budget : categoryBudgetRepository.findAllByCategory_User_Id(userId)) {
            budgetsByCategoryId.put(budget.getCategory().getId(), budget);
        }

        List<MonthlyCategorySummaryResponse> categorySummaries = categoryRepository.findAllByUser_IdOrderByNameAsc(userId).stream()
                .map(category -> buildCategorySummary(category, budgetsByCategoryId.get(category.getId()), month))
                .toList();

        return MonthlyOverviewResponse.builder()
                .month(month)
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .remainingBudget(totalBudget.subtract(totalSpent))
                .overBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0 && totalSpent.compareTo(totalBudget) > 0)
                .totalIncome(totalIncome)
                .configuredCategoryBudgetSum(configuredCategoryBudgetSum)
                .categoryBudgetSumExceedsTotalBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0
                        && configuredCategoryBudgetSum.compareTo(totalBudget) > 0)
                .categories(categorySummaries)
                .build();
    }

    private MonthlyCategorySummaryResponse buildCategorySummary(Category category, CategoryBudget budget, YearMonth month) {
        BigDecimal spent = transactionService.sumExpensesForCategoryAndMonth(category.getId(), month);
        BigDecimal limit = budget != null ? budget.getMonthlyLimit() : null;
        BigDecimal remaining = limit != null ? limit.subtract(spent) : null;
        return MonthlyCategorySummaryResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .monthlyLimit(limit)
                .spent(spent)
                .remaining(remaining)
                .overLimit(limit != null && spent.compareTo(limit) > 0)
                .hasBudget(limit != null)
                .build();
    }

    private BigDecimal getConfiguredCategoryBudgetSum(Long userId) {
        return categoryBudgetRepository.findAllByCategory_User_Id(userId).stream()
                .map(CategoryBudget::getMonthlyLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
