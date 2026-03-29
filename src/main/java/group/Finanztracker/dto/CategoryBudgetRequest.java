package group.Finanztracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBudgetRequest {
    @NotNull(message = "Kategorie-ID darf nicht null sein")
    private Long categoryId;

    @NotNull(message = "Monatslimit darf nicht null sein")
    @DecimalMin(value = "0.01", message = "Monatslimit muss größer als 0 sein")
    @Digits(integer = 10, fraction = 2, message = "Monatslimit darf maximal 10 Vorkomma- und 2 Nachkommastellen haben")
    private BigDecimal monthlyLimit;
}