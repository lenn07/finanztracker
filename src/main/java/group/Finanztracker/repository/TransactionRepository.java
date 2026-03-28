package group.Finanztracker.repository;

import group.Finanztracker.entity.Transaction;
import group.Finanztracker.entity.TransactionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository 
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByCategory_Id(Long categoryId);

    List<Transaction> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Transaction> findAllByType(TransactionType type);

    List<Transaction> findByType(TransactionType type);
}
