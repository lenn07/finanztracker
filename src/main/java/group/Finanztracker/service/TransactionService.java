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
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;
    private final CurrentUserService currentUserService;

    public BigDecimal sumExpensesForMonth(YearMonth month) {
        return transactionRepository.sumAmountByTypeAndDateBetween(
                TransactionType.EXPENSE,
                month.atDay(1),
                month.atEndOfMonth(),
                currentUserService.getCurrentUserId()
        );
    }

    public BigDecimal sumIncomeForMonth(YearMonth month) {
        return transactionRepository.sumAmountByTypeAndDateBetween(
                TransactionType.INCOME,
                month.atDay(1),
                month.atEndOfMonth(),
                currentUserService.getCurrentUserId()
        );
    }

    public BigDecimal sumExpensesForCategoryAndMonth(Long categoryId, YearMonth month) {
        return transactionRepository.sumMonthlyExpensesByCategory(
                categoryId,
                month.atDay(1),
                month.atEndOfMonth(),
                currentUserService.getCurrentUserId()
        );
    }

    public List<TransactionResponse> findAllForMonth(YearMonth month) {
        return transactionRepository.findAllByDateBetweenAndCategory_User_IdOrderByDateDescIdDesc(
                month.atDay(1), month.atEndOfMonth(), currentUserService.getCurrentUserId()
        ).stream().map(transactionMapper::toResponse).toList();
    }

    public List<TransactionResponse> findAllForMonthFiltered(YearMonth month, TransactionType type, Long categoryId) {
        return transactionRepository.findAllByDateRangeFiltered(
                month.atDay(1), month.atEndOfMonth(), currentUserService.getCurrentUserId(), type, categoryId
        ).stream().map(transactionMapper::toResponse).toList();
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
        return transactionRepository.findByIdAndCategory_User_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaktion mit ID " + id + " nicht gefunden"));
    }

    private Category getCategoryOrThrow(Long categoryId) {
        return categoryRepository.findByIdAndUser_Id(categoryId, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kategorie mit ID " + categoryId + " nicht gefunden"));
    }
}
