package group.Finanztracker.service;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.CategoryBudgetMapper;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryBudgetService {

    private final CategoryBudgetRepository categoryBudgetRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryBudgetMapper categoryBudgetMapper;
    private final CurrentUserService currentUserService;

    @Transactional(readOnly = true)
    public List<CategoryBudgetResponse> getAll() {
        return categoryBudgetRepository.findAllByCategory_User_IdOrderByCategory_NameAsc(currentUserService.getCurrentUserId()).stream()
                .map(categoryBudgetMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryBudgetResponse getById(Long id) {
        CategoryBudget entity = categoryBudgetRepository.findByIdAndCategory_User_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("CategoryBudget not found with id: " + id));
        return categoryBudgetMapper.toResponse(entity);
    }

    @Transactional
    public CategoryBudgetResponse create(CategoryBudgetRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        Category category = categoryRepository.findByIdAndUser_Id(request.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        if (categoryBudgetRepository.findByCategoryAndCategory_User_Id(category, userId).isPresent()) {
            throw new IllegalStateException("Für diese Kategorie existiert bereits ein Budget.");
        }
        CategoryBudget entity = categoryBudgetMapper.toEntity(request, category);
        entity = categoryBudgetRepository.save(entity);
        return categoryBudgetMapper.toResponse(entity);
    }

    @Transactional
    public CategoryBudgetResponse update(Long id, CategoryBudgetRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        CategoryBudget entity = categoryBudgetRepository.findByIdAndCategory_User_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("CategoryBudget not found with id: " + id));
        Category category = categoryRepository.findByIdAndUser_Id(request.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        categoryBudgetRepository.findByCategoryAndCategory_User_Id(category, userId)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("Für diese Kategorie existiert bereits ein Budget.");
                });
        categoryBudgetMapper.updateEntity(entity, request, category);
        entity = categoryBudgetRepository.save(entity);
        return categoryBudgetMapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (categoryBudgetRepository.findByIdAndCategory_User_Id(id, currentUserService.getCurrentUserId()).isEmpty()) {
            throw new ResourceNotFoundException("CategoryBudget not found with id: " + id);
        }
        categoryBudgetRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal getConfiguredCategoryBudgetSum() {
        return categoryBudgetRepository.findAllByCategory_User_Id(currentUserService.getCurrentUserId()).stream()
                .map(CategoryBudget::getMonthlyLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
