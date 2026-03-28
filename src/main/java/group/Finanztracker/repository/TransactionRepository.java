package group.Finanztracker.repository;

import group.Finanztracker.entity.Transaction;
import group.Finanztracker.entity.TransactionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository 
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.category.id = :categoryId")
    List<Transaction> findAllByCategory_Id(Long categoryId);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate")
    List<Transaction> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT t FROM Transaction t WHERE t.type = :type")
    List<Transaction> findAllByType(TransactionType type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t where t.type = 'EXPENSE'")
    BigDecimal sumAmountofTransactions();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t where t.type = 'EXPENSE' and t.category.id = :categoryId")
    BigDecimal sumAmountofTransactionsByCategory(Long categoryId);

}
