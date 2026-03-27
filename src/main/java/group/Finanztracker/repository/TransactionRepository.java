package group.Finanztracker.repository;

import group.Finanztracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository 
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
