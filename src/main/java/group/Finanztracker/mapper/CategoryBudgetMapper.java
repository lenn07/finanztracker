package group.Finanztracker.mapper;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import org.springframework.stereotype.Component;

@Component
public class CategoryBudgetMapper {

    public CategoryBudget toEntity(CategoryBudgetRequest request, Category category) {
        return CategoryBudget.builder()
                .category(category)
                .monthlyLimit(request.getMonthlyLimit())
                .build();
    }

    public CategoryBudgetResponse toResponse(CategoryBudget entity) {
        return CategoryBudgetResponse.builder()
                .id(entity.getId())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .monthlyLimit(entity.getMonthlyLimit())
                .build();
    }

    public void updateEntity(CategoryBudget entity, CategoryBudgetRequest request, Category category) {
        entity.setCategory(category);
        entity.setMonthlyLimit(request.getMonthlyLimit());
    }
}