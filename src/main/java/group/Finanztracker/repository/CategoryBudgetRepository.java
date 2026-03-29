package group.Finanztracker.repository;

import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
	Optional<CategoryBudget> findByCategory(Category category);
	void deleteByCategory(Category category);
}
