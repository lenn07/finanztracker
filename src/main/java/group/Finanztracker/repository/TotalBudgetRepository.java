package group.Finanztracker.repository;

import group.Finanztracker.entity.TotalBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TotalBudgetRepository extends JpaRepository<TotalBudget, Long> {
    @Query("SELECT t FROM TotalBudget t WHERE t.user.id = :userId ORDER BY t.id asc")
    Optional<TotalBudget> findFirstByUser_IdOrderByIdAsc(Long userId);

    @Query("SELECT t FROM TotalBudget t WHERE t.id = :id AND t.user.id = :userId")
    Optional<TotalBudget> findByIdAndUser_Id(Long id, Long userId);

    @Modifying
    @Query(value = """
            update budget_settings
            set user_id = :userId
            where user_id is null
            """, nativeQuery = true)
    int assignUserToOrphanedBudgets(Long userId);
}
