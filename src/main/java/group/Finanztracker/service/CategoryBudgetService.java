package group.Finanztracker.service;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.CategoryBudgetMapper;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryBudgetService {

	private final CategoryBudgetRepository categoryBudgetRepository;
	private final CategoryRepository categoryRepository;
	private final CategoryBudgetMapper categoryBudgetMapper;

	@Transactional(readOnly = true)
	public List<CategoryBudgetResponse> getAll() {
		return categoryBudgetRepository.findAll().stream()
				.map(categoryBudgetMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public CategoryBudgetResponse getById(Long id) {
		CategoryBudget entity = categoryBudgetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CategoryBudget not found with id: " + id));
		return categoryBudgetMapper.toResponse(entity);
	}

	@Transactional
	public CategoryBudgetResponse create(CategoryBudgetRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
        if (categoryBudgetRepository.findByCategory(category).isPresent()) {
            throw new IllegalStateException("Für diese Kategorie existiert bereits ein Budget.");
        }
        CategoryBudget entity = categoryBudgetMapper.toEntity(request, category);
        entity = categoryBudgetRepository.save(entity);
        return categoryBudgetMapper.toResponse(entity);
	}

	@Transactional
	public CategoryBudgetResponse update(Long id, CategoryBudgetRequest request) {
		CategoryBudget entity = categoryBudgetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CategoryBudget not found with id: " + id));
		Category category = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
		categoryBudgetMapper.updateEntity(entity, request, category);
		entity = categoryBudgetRepository.save(entity);
		return categoryBudgetMapper.toResponse(entity);
	}

	@Transactional
	public void delete(Long id) {
		if (!categoryBudgetRepository.existsById(id)) {
			throw new ResourceNotFoundException("CategoryBudget not found with id: " + id);
		}
		categoryBudgetRepository.deleteById(id);
	}
}
