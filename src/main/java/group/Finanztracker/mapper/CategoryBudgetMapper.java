package group.Finanztracker.mapper;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CategoryBudgetMapper {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public CategoryBudget toEntity(CategoryBudgetRequest request, Category category) {
        return CategoryBudget.builder()
                .category(category)
                .percentage(request.getPercentage())
                .build();
    }

    public CategoryBudgetResponse toResponse(CategoryBudget entity, BigDecimal totalMonthlyLimit) {
        return CategoryBudgetResponse.builder()
                .id(entity.getId())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .percentage(entity.getPercentage())
                .calculatedMonthlyLimit(calculateMonthlyLimit(entity.getPercentage(), totalMonthlyLimit))
                .build();
    }

    public void updateEntity(CategoryBudget entity, CategoryBudgetRequest request, Category category) {
        entity.setCategory(category);
        entity.setPercentage(request.getPercentage());
    }

    public BigDecimal calculateMonthlyLimit(BigDecimal percentage, BigDecimal totalMonthlyLimit) {
        if (percentage == null || totalMonthlyLimit == null || totalMonthlyLimit.signum() <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return totalMonthlyLimit.multiply(percentage).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }
}
