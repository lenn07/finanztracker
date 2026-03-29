package group.Finanztracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "category_budget_limits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBudget {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false, unique = true)
	private Category category;

	@Column(name = "monthly_limit", nullable = false, precision = 12, scale = 2)
	private BigDecimal monthlyLimit;
}
