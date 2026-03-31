package group.Finanztracker.repository;

import group.Finanztracker.entity.TotalBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TotalBudgetRepository extends JpaRepository<TotalBudget, Long> {
    Optional<TotalBudget> findFirstByOrderByIdAsc();
}
