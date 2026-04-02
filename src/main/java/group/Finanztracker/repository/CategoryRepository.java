package group.Finanztracker.repository;

import group.Finanztracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser_IdOrderByNameAsc(Long userId);
    Optional<Category> findByUser_IdAndNameIgnoreCase(Long userId, String name);
    Optional<Category> findByIdAndUser_Id(Long id, Long userId);

    @Modifying
    @Query(value = """
            update categories
            set user_id = :userId
            where user_id is null
            """, nativeQuery = true)
    int assignUserToOrphanedCategories(Long userId);
}
