package group.Finanztracker.controller;

import group.Finanztracker.dto.TransactionRequest;
import group.Finanztracker.dto.TransactionResponse;
import group.Finanztracker.entity.TransactionType;
import group.Finanztracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionResponse>> getByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(transactionService.findAllByCategoryId(categoryId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponse>> getByType(@PathVariable TransactionType type) {
        return ResponseEntity.ok(transactionService.findAllByType(type));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getByDateRange(@RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(transactionService.findAllByDateRange(startDate, endDate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping("/sum/{categoryId}")
    public ResponseEntity<String> getSumOfTransactionsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(transactionService.sumAmountOfTransactionsByCategory(categoryId).toString());
    }
    

    @GetMapping("/sum")
    public ResponseEntity<String> getSumOfTransactions() {
        return ResponseEntity.ok(transactionService.sumAmountOfTransactions().toString());
    }


    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
                                                      @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
