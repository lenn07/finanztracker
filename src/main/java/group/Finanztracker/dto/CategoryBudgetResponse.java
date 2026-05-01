package group.Finanztracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBudgetResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private BigDecimal percentage;
    private BigDecimal calculatedMonthlyLimit;
}
