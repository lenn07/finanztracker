package group.Finanztracker.service;

import group.Finanztracker.repository.security.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AppUserRepository appUserRepository;

    @Transactional
    public void deleteAccount(Long userId) {
        appUserRepository.deleteById(userId);
    }
}
