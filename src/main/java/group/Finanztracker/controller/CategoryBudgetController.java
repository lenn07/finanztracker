package group.Finanztracker.controller;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.service.CategoryBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets/category")
@RequiredArgsConstructor
public class CategoryBudgetController {

	private final CategoryBudgetService categoryBudgetService;

	@GetMapping
	public ResponseEntity<List<CategoryBudgetResponse>> getAll() {
		return ResponseEntity.ok(categoryBudgetService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryBudgetResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(categoryBudgetService.getById(id));
	}

	@PostMapping
	public ResponseEntity<CategoryBudgetResponse> create(@Valid @RequestBody CategoryBudgetRequest request) {
		CategoryBudgetResponse response = categoryBudgetService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryBudgetResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryBudgetRequest request) {
		return ResponseEntity.ok(categoryBudgetService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		categoryBudgetService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
