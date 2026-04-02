package group.Finanztracker.repository.security;

import group.Finanztracker.entity.security.AppUser;
import group.Finanztracker.entity.security.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);
    void deleteByUser(AppUser user);
}
