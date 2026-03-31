package group.Finanztracker.service;

import group.Finanztracker.dto.MonthlyCategorySummaryResponse;
import group.Finanztracker.dto.MonthlyOverviewResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyOverviewServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryBudgetRepository categoryBudgetRepository;
    @Mock
    private TransactionService transactionService;
    @Mock
    private TotalBudgetService totalBudgetService;

    private MonthlyOverviewService monthlyOverviewService;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-03-15T12:00:00Z"), ZoneId.of("Europe/Berlin"));
        monthlyOverviewService = new MonthlyOverviewService(
                categoryRepository,
                categoryBudgetRepository,
                transactionService,
                totalBudgetService,
                clock
        );
    }

    @Test
    void shouldBuildMonthlyOverviewWithExpenseOnlyBudgetLogic() {
        YearMonth month = YearMonth.of(2026, 3);
        Category groceries = Category.builder().id(1L).name("Lebensmittel").build();
        Category salary = Category.builder().id(2L).name("Gehalt").build();
        CategoryBudget groceriesBudget = CategoryBudget.builder()
                .id(10L)
                .category(groceries)
                .monthlyLimit(new BigDecimal("300.00"))
                .build();

        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(salary, groceries));
        when(categoryBudgetRepository.findAll()).thenReturn(List.of(groceriesBudget));
        when(totalBudgetService.getCurrentBudgetLimit()).thenReturn(new BigDecimal("1000.00"));
        when(transactionService.sumExpensesForMonth(month)).thenReturn(new BigDecimal("350.00"));
        when(transactionService.sumIncomeForMonth(month)).thenReturn(new BigDecimal("2500.00"));
        when(transactionService.sumExpensesForCategoryAndMonth(1L, month)).thenReturn(new BigDecimal("350.00"));
        when(transactionService.sumExpensesForCategoryAndMonth(2L, month)).thenReturn(BigDecimal.ZERO);

        MonthlyOverviewResponse overview = monthlyOverviewService.getOverview(month);

        assertThat(overview.getMonth()).isEqualTo(month);
        assertThat(overview.getTotalBudget()).isEqualByComparingTo("1000.00");
        assertThat(overview.getTotalSpent()).isEqualByComparingTo("350.00");
        assertThat(overview.getTotalIncome()).isEqualByComparingTo("2500.00");
        assertThat(overview.getRemainingBudget()).isEqualByComparingTo("650.00");
        assertThat(overview.isOverBudget()).isFalse();
        assertThat(overview.getConfiguredCategoryBudgetSum()).isEqualByComparingTo("300.00");

        MonthlyCategorySummaryResponse groceriesSummary = overview.getCategories().stream()
                .filter(category -> category.getCategoryId().equals(1L))
                .findFirst()
                .orElseThrow();
        assertThat(groceriesSummary.isHasBudget()).isTrue();
        assertThat(groceriesSummary.getRemaining()).isEqualByComparingTo("-50.00");
        assertThat(groceriesSummary.isOverLimit()).isTrue();

        MonthlyCategorySummaryResponse salarySummary = overview.getCategories().stream()
                .filter(category -> category.getCategoryId().equals(2L))
                .findFirst()
                .orElseThrow();
        assertThat(salarySummary.isHasBudget()).isFalse();
        assertThat(salarySummary.getMonthlyLimit()).isNull();
    }
}
