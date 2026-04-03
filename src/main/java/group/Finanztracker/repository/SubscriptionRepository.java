package group.Finanztracker.repository;

import group.Finanztracker.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUser_IdOrderByActiveDescTitleAsc(Long userId);
    List<Subscription> findAllByUser_IdAndActiveTrueOrderByIdAsc(Long userId);
    Optional<Subscription> findByIdAndUser_Id(Long id, Long userId);
}
