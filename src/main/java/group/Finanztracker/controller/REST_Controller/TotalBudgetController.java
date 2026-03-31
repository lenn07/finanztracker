package group.Finanztracker.controller.REST_Controller;

import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.service.TotalBudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets/total")
@RequiredArgsConstructor
public class TotalBudgetController {

	private final TotalBudgetService totalBudgetService;

	@GetMapping
	public ResponseEntity<List<TotalBudgetResponse>> getAll() {
		return ResponseEntity.ok(totalBudgetService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<TotalBudgetResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(totalBudgetService.getById(id));
	}

	@PostMapping
	public ResponseEntity<TotalBudgetResponse> create(@Valid @RequestBody TotalBudgetRequest request) {
		TotalBudgetResponse response = totalBudgetService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TotalBudgetResponse> update(@PathVariable Long id,
													  @Valid @RequestBody TotalBudgetRequest request) {
		return ResponseEntity.ok(totalBudgetService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		totalBudgetService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
