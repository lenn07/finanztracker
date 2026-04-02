package group.Finanztracker.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
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

    public void sendVerificationMail(String recipient, String verificationUrl) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            log.info("Mailversand nicht konfiguriert. Verifikationslink fuer {}: {}", recipient, verificationUrl);
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
        mailSender.send(message);
    }
}
