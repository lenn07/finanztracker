package group.Finanztracker.entity;

import group.Finanztracker.entity.security.AppUser;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budget_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_monthly_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalMonthlyLimit;

    @Column(name = "rollover_enabled", nullable = false)
    private boolean rolloverEnabled;

    @Column(name = "rollover_start_month")
    private LocalDate rolloverStartMonth;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;
}
