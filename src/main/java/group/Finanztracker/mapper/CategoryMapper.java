package group.Finanztracker.mapper;

import group.Finanztracker.dto.CategoryRequest;
import group.Finanztracker.dto.CategoryResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.security.AppUser;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request, AppUser user) {
        return Category.builder()
                .name(request.getName())
                .user(user)
                .build();
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.getName());
    }
}
