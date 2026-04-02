package group.Finanztracker.service;

import group.Finanztracker.dto.CategoryRequest;
import group.Finanztracker.dto.CategoryResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.CategoryMapper;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.TransactionRepository;
import group.Finanztracker.repository.security.AppUserRepository;
import group.Finanztracker.service.security.CurrentUserService;
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
    private final CurrentUserService currentUserService;
    private final AppUserRepository appUserRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAllByUser_IdOrderByNameAsc(currentUserService.getCurrentUserId()).stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse findById(Long id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        validateUniqueName(request.getName(), null);
        Category category = categoryMapper.toEntity(
                request,
                appUserRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"))
        );
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
        Long userId = currentUserService.getCurrentUserId();
        Category category = getCategoryOrThrow(id);
        if (transactionRepository.existsByCategory_IdAndCategory_User_Id(id, userId)) {
            throw new IllegalStateException("Kategorie kann nicht gelöscht werden, solange noch Transaktionen zugeordnet sind.");
        }
        categoryBudgetRepository.findByCategoryAndCategory_User_Id(category, userId)
                .ifPresent(categoryBudgetRepository::delete);
        categoryRepository.delete(category);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findByIdAndUser_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + id + " nicht gefunden"));
    }

    private void validateUniqueName(String name, Long currentId) {
        categoryRepository.findByUser_IdAndNameIgnoreCase(currentUserService.getCurrentUserId(), name.trim())
                .filter(existing -> !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new IllegalStateException("Eine Kategorie mit diesem Namen existiert bereits.");
                });
    }
}
