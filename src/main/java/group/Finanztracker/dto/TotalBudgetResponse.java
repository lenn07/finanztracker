package group.Finanztracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalBudgetResponse {
    private Long id;
    private BigDecimal totalMonthlyLimit;
}