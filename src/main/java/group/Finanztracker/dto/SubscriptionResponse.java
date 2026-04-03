package group.Finanztracker.dto;

import group.Finanztracker.entity.SubscriptionInterval;
import group.Finanztracker.entity.TransactionType;
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
public class SubscriptionResponse {

    private Long id;
    private String title;
    private BigDecimal amount;
    private TransactionType type;
    private Long categoryId;
    private String categoryName;
    private SubscriptionInterval interval;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;
    private boolean active;
}
