package group.Finanztracker.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationForm {

    @NotBlank(message = "Bitte gib deine E-Mail-Adresse ein.")
    @Email(message = "Bitte gib eine gültige E-Mail-Adresse ein.")
    private String email;

    @NotBlank(message = "Bitte gib ein Passwort ein.")
    @Size(min = 8, message = "Das Passwort muss mindestens 8 Zeichen lang sein.")
    private String password;

    @NotBlank(message = "Bitte bestätige dein Passwort.")
    private String confirmPassword;

    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
