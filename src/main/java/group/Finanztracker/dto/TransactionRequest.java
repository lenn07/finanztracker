package group.Finanztracker.dto;

import group.Finanztracker.entity.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    @NotBlank(message = "Titel darf nicht leer sein")
    @Size(max = 255, message = "Titel darf maximal 255 Zeichen lang sein")
    private String title;

    @NotNull(message = "Betrag darf nicht null sein")
    @DecimalMin(value = "0.01", message = "Betrag muss größer als 0 sein")
    @Digits(integer = 10, fraction = 2, message = "Betrag darf maximal 10 Vorkomma- und 2 Nachkommastellen haben")
    private BigDecimal amount;

    @NotNull(message = "Typ darf nicht null sein")
    private TransactionType type;

    @NotNull(message = "Kategorie-ID darf nicht null sein")
    private Long categoryId;

    @NotNull(message = "Datum darf nicht null sein")
    private LocalDate date;

    private String note;
}
