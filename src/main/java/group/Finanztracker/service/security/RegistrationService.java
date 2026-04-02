package group.Finanztracker.service.security;

import group.Finanztracker.dto.security.RegistrationForm;
import group.Finanztracker.entity.security.AppUser;
import group.Finanztracker.entity.security.AppUserRole;
import group.Finanztracker.entity.security.EmailVerificationToken;
import group.Finanztracker.repository.security.AppUserRepository;
import group.Finanztracker.repository.security.EmailVerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationMailService verificationMailService;
    private final Clock clock;

    @Transactional
    public void register(RegistrationForm form, String appBaseUrl) {
        String email = normalizeEmail(form.getEmail());
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalStateException("Zu dieser E-Mail-Adresse existiert bereits ein Konto.");
        }

        Instant now = Instant.now(clock);
        AppUser user = appUserRepository.save(AppUser.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(form.getPassword()))
                .role(AppUserRole.USER)
                .emailVerified(false)
                .createdAt(now)
                .build());

        EmailVerificationToken token = emailVerificationTokenRepository.save(EmailVerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .createdAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .build());

        verificationMailService.sendVerificationMail(email, appBaseUrl + "/auth/verify?token=" + token.getToken());
    }

    @Transactional
    public void resendVerificationMail(String rawEmail, String appBaseUrl) {
        String email = normalizeEmail(rawEmail);
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Zu dieser E-Mail-Adresse wurde kein Konto gefunden."));
        if (user.isEmailVerified()) {
            throw new IllegalStateException("Dieses Konto wurde bereits bestaetigt.");
        }

        emailVerificationTokenRepository.deleteByUser(user);
        Instant now = Instant.now(clock);
        EmailVerificationToken token = emailVerificationTokenRepository.save(EmailVerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .createdAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .build());

        verificationMailService.sendVerificationMail(email, appBaseUrl + "/auth/verify?token=" + token.getToken());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
