package group.Finanztracker.service;

import group.Finanztracker.dto.TransactionRequest;
import group.Finanztracker.dto.TransactionResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.Transaction;
import group.Finanztracker.entity.TransactionType;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.TransactionMapper;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    public List<TransactionResponse> findAll() {
        return transactionRepository.findAllByOrderByDateDescIdDesc().stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> findAllByCategoryId(Long categoryId) {
        return transactionRepository.findAllByCategory_IdOrderByDateDescIdDesc(categoryId).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findAllByDateBetweenOrderByDateDescIdDesc(startDate, endDate).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> findAllByType(TransactionType type) {
        return transactionRepository.findAllByTypeOrderByDateDescIdDesc(type).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public BigDecimal sumAmountOfTransactions() {
        return transactionRepository.sumAmountofTransactions();
    }

    public BigDecimal sumAmountOfTransactionsByCategory(Long categoryId) {
        return transactionRepository.sumAmountofTransactionsByCategory(categoryId);
    }

    public BigDecimal sumExpensesForMonth(YearMonth month) {
        return transactionRepository.sumAmountByTypeAndDateBetween(
                TransactionType.EXPENSE,
                month.atDay(1),
                month.atEndOfMonth()
        );
    }

    public BigDecimal sumIncomeForMonth(YearMonth month) {
        return transactionRepository.sumAmountByTypeAndDateBetween(
                TransactionType.INCOME,
                month.atDay(1),
                month.atEndOfMonth()
        );
    }

    public BigDecimal sumExpensesForCategoryAndMonth(Long categoryId, YearMonth month) {
        return transactionRepository.sumMonthlyExpensesByCategory(
                categoryId,
                month.atDay(1),
                month.atEndOfMonth()
        );
    }

    public List<TransactionResponse> findAllForMonth(YearMonth month) {
        return findAllByDateRange(month.atDay(1), month.atEndOfMonth());
    }

    public List<TransactionResponse> findAllForMonthFiltered(YearMonth month, TransactionType type, Long categoryId) {
        return findAllForMonth(month).stream()
                .filter(transaction -> type == null || transaction.getType() == type)
                .filter(transaction -> categoryId == null || transaction.getCategoryId().equals(categoryId))
                .toList();
    }

    public TransactionResponse findById(Long id) {
        Transaction transaction = getTransactionOrThrow(id);
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        Category category = getCategoryOrThrow(request.getCategoryId());
        Transaction transaction = transactionMapper.toEntity(request, category);
        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toResponse(saved);
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = getTransactionOrThrow(id);
        Category category = getCategoryOrThrow(request.getCategoryId());
        transactionMapper.updateEntity(transaction, request, category);
        Transaction saved = transactionRepository.save(transaction);
        return transactionMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = getTransactionOrThrow(id);
        transactionRepository.delete(transaction);
    }

    private Transaction getTransactionOrThrow(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaktion mit ID " + id + " nicht gefunden"));
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + categoryId + " nicht gefunden"));
    }
}
