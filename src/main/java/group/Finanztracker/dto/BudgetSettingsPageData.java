package group.Finanztracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetSettingsPageData {

    private Long totalBudgetId;
    private BigDecimal totalMonthlyLimit;
    private BigDecimal configuredCategoryBudgetSum;
    private boolean categoryBudgetSumExceedsTotalBudget;
    private List<CategoryBudgetResponse> categoryBudgets;
}
