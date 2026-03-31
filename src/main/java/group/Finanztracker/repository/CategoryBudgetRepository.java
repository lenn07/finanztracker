package group.Finanztracker.repository;

import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
	Optional<CategoryBudget> findByCategory(Category category);
	List<CategoryBudget> findAllByOrderByCategory_NameAsc();
	void deleteByCategory(Category category);
}
