package group.Finanztracker.service.security;

import group.Finanztracker.repository.security.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final AppUserRepository appUserRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Kein eingeloggter Benutzer vorhanden.");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUserPrincipal securityUserPrincipal) {
            return securityUserPrincipal.getId();
        }
        throw new IllegalStateException("Benutzerkontext konnte nicht aufgelöst werden.");
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUserPrincipal securityUserPrincipal) {
            return securityUserPrincipal.getUsername();
        }
        return null;
    }

    @Transactional
    public void updateLastLogin(Long userId) {
        appUserRepository.findById(userId).ifPresent(user -> user.setLastLoginAt(java.time.Instant.now()));
    }
}
