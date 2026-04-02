package group.Finanztracker.dto.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResendVerificationForm {

    @NotBlank(message = "Bitte gib deine E-Mail-Adresse ein.")
    @Email(message = "Bitte gib eine gültige E-Mail-Adresse ein.")
    private String email;
}
