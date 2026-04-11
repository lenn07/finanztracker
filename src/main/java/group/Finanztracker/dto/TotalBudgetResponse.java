package group.Finanztracker.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalBudgetResponse {
    private Long id;
    private BigDecimal totalMonthlyLimit;
    private boolean rolloverEnabled;
    private LocalDate rolloverStartMonth;
}
