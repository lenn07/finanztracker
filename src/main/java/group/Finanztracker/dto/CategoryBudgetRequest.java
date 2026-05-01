package group.Finanztracker.dto;

import jakarta.validation.constraints.DecimalMax;
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

    @NotNull(message = "Prozentsatz darf nicht null sein")
    @DecimalMin(value = "0.01", message = "Prozentsatz muss größer als 0 sein")
    @DecimalMax(value = "100.00", message = "Prozentsatz darf maximal 100 sein")
    @Digits(integer = 3, fraction = 2, message = "Prozentsatz darf maximal 3 Vorkomma- und 2 Nachkommastellen haben")
    private BigDecimal percentage;
}
