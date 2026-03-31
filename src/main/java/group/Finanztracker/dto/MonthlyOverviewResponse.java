package group.Finanztracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyOverviewResponse {

    private YearMonth month;
    private BigDecimal totalBudget;
    private BigDecimal totalSpent;
    private BigDecimal remainingBudget;
    private boolean overBudget;
    private BigDecimal totalIncome;
    private BigDecimal configuredCategoryBudgetSum;
    private boolean categoryBudgetSumExceedsTotalBudget;
    private List<MonthlyCategorySummaryResponse> categories;
}
