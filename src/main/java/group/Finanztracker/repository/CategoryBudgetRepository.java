package group.Finanztracker.repository;

import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
	Optional<CategoryBudget> findByCategoryAndCategory_User_Id(Category category, Long userId);
	List<CategoryBudget> findAllByCategory_User_IdOrderByCategory_NameAsc(Long userId);
	List<CategoryBudget> findAllByCategory_User_Id(Long userId);
	Optional<CategoryBudget> findByIdAndCategory_User_Id(Long id, Long userId);
}
