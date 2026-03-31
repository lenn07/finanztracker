package group.Finanztracker.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalBudgetForm {

    @NotNull(message = "Gesamtlimit darf nicht null sein")
    @DecimalMin(value = "0.01", message = "Gesamtlimit muss größer als 0 sein")
    @Digits(integer = 10, fraction = 2, message = "Gesamtlimit darf maximal 10 Vorkomma- und 2 Nachkommastellen haben")
    private BigDecimal totalMonthlyLimit;
}
