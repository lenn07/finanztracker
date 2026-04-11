package group.Finanztracker.service;

import group.Finanztracker.dto.MonthlyCategorySummaryResponse;
import group.Finanztracker.dto.MonthlyOverviewResponse;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonthlyOverviewService {

    private final CategoryRepository categoryRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final TransactionService transactionService;
    private final TotalBudgetService totalBudgetService;
    private final CurrentUserService currentUserService;

    public MonthlyOverviewResponse getOverview(YearMonth month) {
        Long userId = currentUserService.getCurrentUserId();

        Optional<TotalBudgetResponse> budgetOpt = totalBudgetService.getCurrentBudget();
        BigDecimal totalBudget = budgetOpt.map(TotalBudgetResponse::getTotalMonthlyLimit).orElse(BigDecimal.ZERO);
        BigDecimal totalSpent = transactionService.sumExpensesForMonth(month);
        BigDecimal totalIncome = transactionService.sumIncomeForMonth(month);

        BigDecimal rolloverAmount = budgetOpt.map(b -> calculateRollover(b, month)).orElse(BigDecimal.ZERO);
        boolean rolloverEnabled = budgetOpt.map(TotalBudgetResponse::isRolloverEnabled).orElse(false);
        BigDecimal effectiveBudget = totalBudget.add(rolloverAmount).max(BigDecimal.ZERO);

        List<CategoryBudget> allBudgets = categoryBudgetRepository.findAllByCategory_User_Id(userId);

        Map<Long, CategoryBudget> budgetsByCategoryId = new HashMap<>();
        for (CategoryBudget budget : allBudgets) {
            budgetsByCategoryId.put(budget.getCategory().getId(), budget);
        }

        BigDecimal configuredCategoryBudgetSum = allBudgets.stream()
                .map(CategoryBudget::getMonthlyLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MonthlyCategorySummaryResponse> categorySummaries = categoryRepository.findAllByUser_IdOrderByNameAsc(userId).stream()
                .map(category -> buildCategorySummary(category, budgetsByCategoryId.get(category.getId()), month))
                .toList();

        return MonthlyOverviewResponse.builder()
                .month(month)
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .remainingBudget(totalBudget.subtract(totalSpent))
                .overBudget(effectiveBudget.compareTo(BigDecimal.ZERO) > 0 && totalSpent.compareTo(effectiveBudget) > 0)
                .totalIncome(totalIncome)
                .configuredCategoryBudgetSum(configuredCategoryBudgetSum)
                .categoryBudgetSumExceedsTotalBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0
                        && configuredCategoryBudgetSum.compareTo(totalBudget) > 0)
                .categories(categorySummaries)
                .effectiveBudget(effectiveBudget)
                .rolloverEnabled(rolloverEnabled)
                .build();
    }

    private BigDecimal calculateRollover(TotalBudgetResponse budget, YearMonth currentMonth) {
        if (!budget.isRolloverEnabled() || budget.getRolloverStartMonth() == null) {
            return BigDecimal.ZERO;
        }
        YearMonth startMonth = YearMonth.from(budget.getRolloverStartMonth());
        if (!startMonth.isBefore(currentMonth)) {
            return BigDecimal.ZERO;
        }
        LocalDate rangeStart = startMonth.atDay(1);
        LocalDate rangeEnd = currentMonth.minusMonths(1).atEndOfMonth();
        Map<YearMonth, BigDecimal> spentByMonth = transactionService.sumExpensesGroupedByMonth(rangeStart, rangeEnd);
        BigDecimal rollover = BigDecimal.ZERO;
        YearMonth m = startMonth;
        while (m.isBefore(currentMonth)) {
            BigDecimal spent = spentByMonth.getOrDefault(m, BigDecimal.ZERO);
            rollover = rollover.add(budget.getTotalMonthlyLimit().subtract(spent));
            m = m.plusMonths(1);
        }
        return rollover;
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
}
