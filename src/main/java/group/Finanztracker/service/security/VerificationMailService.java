package group.Finanztracker.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationMailService {

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    @Value("${app.mail.from:no-reply@finanztracker.local}")
    private String mailFrom;
    @Value("${app.mail.required:true}")
    private boolean mailRequired;
    @Value("${spring.mail.host:}")
    private String mailHost;
    @Value("${spring.mail.username:}")
    private String mailUsername;
    @Value("${spring.mail.password:}")
    private String mailPassword;

    public void sendVerificationMail(String recipient, String verificationUrl) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null || !isMailConfigured()) {
            handleMissingMailConfiguration(recipient, verificationUrl);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(recipient);
        message.setSubject("Bitte bestaetige deine Registrierung");
        message.setText("""
                Willkommen beim Finanztracker.

                Bitte bestaetige deine E-Mail-Adresse ueber diesen Link:
                %s

                Der Link ist 24 Stunden gueltig.
                """.formatted(verificationUrl));
        try {
            mailSender.send(message);
        } catch (MailAuthenticationException ex) {
            throw new IllegalStateException("Der Mailversand ist aktuell nicht korrekt authentifiziert. Bitte pruefe die SMTP-Zugangsdaten.", ex);
        } catch (MailException ex) {
            throw new IllegalStateException("Der Mailversand ist aktuell nicht verfuegbar. Bitte versuche es spaeter erneut.", ex);
        }
    }

    private boolean isMailConfigured() {
        return hasText(mailHost) && hasText(mailUsername) && hasText(mailPassword);
    }

    private void handleMissingMailConfiguration(String recipient, String verificationUrl) {
        if (mailRequired) {
            throw new IllegalStateException("Der Mailversand ist noch nicht vollstaendig konfiguriert. Bitte hinterlege SMTP-Zugangsdaten.");
        }
        log.info("Mailversand nicht konfiguriert. Verifikationslink fuer {}: {}", recipient, verificationUrl);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
