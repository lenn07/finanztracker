package group.Finanztracker.repository;

import group.Finanztracker.entity.Transaction;
import group.Finanztracker.entity.TransactionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository 
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.category.id = :categoryId AND t.category.user.id = :userId")
    List<Transaction> findAllByCategory_IdAndCategory_User_Id(Long categoryId, Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.category.user.id = :userId")
    List<Transaction> findAllByDateBetweenAndCategory_User_Id(LocalDate startDate, LocalDate endDate, Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.category.user.id = :userId")
    List<Transaction> findAllByTypeAndCategory_User_Id(TransactionType type, Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t where t.type = 'EXPENSE' and t.category.user.id = :userId")
    BigDecimal sumAmountofTransactions(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t where t.type = 'EXPENSE' and t.category.id = :categoryId and t.category.user.id = :userId")
    BigDecimal sumAmountofTransactionsByCategory(Long categoryId, Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.type = :type
              AND t.category.user.id = :userId
              AND t.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal sumAmountByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate, Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.type = 'EXPENSE'
              AND t.category.id = :categoryId
              AND t.category.user.id = :userId
              AND t.date BETWEEN :startDate AND :endDate
            """)
    BigDecimal sumMonthlyExpensesByCategory(Long categoryId, LocalDate startDate, LocalDate endDate, Long userId);

    List<Transaction> findAllByDateBetweenAndCategory_User_IdOrderByDateDescIdDesc(LocalDate startDate, LocalDate endDate, Long userId);

    List<Transaction> findAllByCategory_User_IdOrderByDateDescIdDesc(Long userId);

    List<Transaction> findAllByCategory_IdAndCategory_User_IdOrderByDateDescIdDesc(Long categoryId, Long userId);

    List<Transaction> findAllByTypeAndCategory_User_IdOrderByDateDescIdDesc(TransactionType type, Long userId);

    boolean existsByCategory_IdAndCategory_User_Id(Long categoryId, Long userId);

    boolean existsBySubscription_IdAndDateBetween(Long subscriptionId, LocalDate startDate, LocalDate endDate);

    Optional<Transaction> findByIdAndCategory_User_Id(Long id, Long userId);
}
