package group.Finanztracker.dto;

import group.Finanztracker.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private String title;
    private BigDecimal amount;
    private TransactionType type;
    private Long categoryId;
    private String categoryName;
    private boolean generatedFromSubscription;
    private LocalDate date;
    private String note;
}
