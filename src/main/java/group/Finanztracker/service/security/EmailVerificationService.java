package group.Finanztracker.service.security;

import group.Finanztracker.entity.security.AppUser;
import group.Finanztracker.entity.security.EmailVerificationToken;
import group.Finanztracker.repository.security.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final ExistingDataOwnershipService existingDataOwnershipService;
    private final Clock clock;

    @Transactional
    public void verifyToken(String tokenValue) {
        EmailVerificationToken token = emailVerificationTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new IllegalStateException("Der Bestaetigungslink ist ungueltig."));

        Instant now = Instant.now(clock);
        if (token.isConfirmed()) {
            throw new IllegalStateException("Diese E-Mail-Adresse wurde bereits bestaetigt.");
        }
        if (token.isExpired(now)) {
            throw new IllegalStateException("Der Bestaetigungslink ist abgelaufen.");
        }

        AppUser user = token.getUser();
        user.setEmailVerified(true);
        user.setVerifiedAt(now);
        token.setConfirmedAt(now);
        existingDataOwnershipService.assignOrphanedDataToFirstVerifiedUser(user.getId());
    }
}
