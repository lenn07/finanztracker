package group.Finanztracker.dto;

import group.Finanztracker.entity.SubscriptionInterval;
import group.Finanztracker.entity.TransactionType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionForm {

    @NotBlank(message = "Titel darf nicht leer sein")
    @Size(max = 255, message = "Titel darf maximal 255 Zeichen lang sein")
    private String title;

    @NotNull(message = "Betrag darf nicht null sein")
    @DecimalMin(value = "0.01", message = "Betrag muss groesser als 0 sein")
    @Digits(integer = 10, fraction = 2, message = "Betrag darf maximal 10 Vorkomma- und 2 Nachkommastellen haben")
    private BigDecimal amount;

    @NotNull(message = "Typ darf nicht null sein")
    private TransactionType type;

    @NotNull(message = "Kategorie-ID darf nicht null sein")
    private Long categoryId;

    @NotNull(message = "Intervall darf nicht null sein")
    private SubscriptionInterval interval;

    @NotNull(message = "Startdatum darf nicht null sein")
    private LocalDate startDate;

    private LocalDate endDate;

    private String note;

    @Builder.Default
    private boolean active = true;

    @AssertTrue(message = "Enddatum darf nicht vor dem Startdatum liegen")
    public boolean isDateRangeValid() {
        return endDate == null || startDate == null || !endDate.isBefore(startDate);
    }
}
