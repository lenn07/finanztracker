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

    @Query("""
            SELECT t FROM Transaction t
            WHERE t.category.user.id = :userId
              AND t.date BETWEEN :startDate AND :endDate
              AND (:type IS NULL OR t.type = :type)
              AND (:categoryId IS NULL OR t.category.id = :categoryId)
            ORDER BY t.date DESC, t.id DESC
            """)
    List<Transaction> findAllByDateRangeFiltered(LocalDate startDate, LocalDate endDate, Long userId, TransactionType type, Long categoryId);

    List<Transaction> findAllByDateBetweenAndCategory_User_IdOrderByDateDescIdDesc(LocalDate startDate, LocalDate endDate, Long userId);

    boolean existsByCategory_IdAndCategory_User_Id(Long categoryId, Long userId);

    boolean existsBySubscription_IdAndDateBetween(Long subscriptionId, LocalDate startDate, LocalDate endDate);

    Optional<Transaction> findByIdAndCategory_User_Id(Long id, Long userId);
}
