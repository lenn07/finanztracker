package group.Finanztracker.service;

import group.Finanztracker.dto.CategoryRequest;
import group.Finanztracker.dto.CategoryResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.CategoryMapper;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse findById(Long id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        validateUniqueName(request.getName(), null);
        Category category = categoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategoryOrThrow(id);
        validateUniqueName(request.getName(), id);
        categoryMapper.updateEntity(category, request);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Category category = getCategoryOrThrow(id);
        if (transactionRepository.existsByCategory_Id(id)) {
            throw new IllegalStateException("Kategorie kann nicht gelöscht werden, solange noch Transaktionen zugeordnet sind.");
        }
        categoryBudgetRepository.findByCategory(category)
                .ifPresent(categoryBudgetRepository::delete);
        categoryRepository.delete(category);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + id + " nicht gefunden"));
    }

    private void validateUniqueName(String name, Long currentId) {
        categoryRepository.findByNameIgnoreCase(name.trim())
                .filter(existing -> !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new IllegalStateException("Eine Kategorie mit diesem Namen existiert bereits.");
                });
    }
}
