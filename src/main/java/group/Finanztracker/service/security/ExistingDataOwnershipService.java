package group.Finanztracker.service.security;

import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.repository.security.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExistingDataOwnershipService {

    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final TotalBudgetRepository totalBudgetRepository;

    @Transactional
    public void assignOrphanedDataToFirstVerifiedUser(Long userId) {
        if (appUserRepository.countByEmailVerifiedTrue() != 1) {
            return;
        }
        categoryRepository.assignUserToOrphanedCategories(userId);
        totalBudgetRepository.assignUserToOrphanedBudgets(userId);
    }
}
