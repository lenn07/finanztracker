package group.Finanztracker.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolloverSettingsForm {
    private boolean rolloverEnabled;
    // Format: "yyyy-MM" — kommt von <input type="month">
    @Pattern(regexp = "^$|^\\d{4}-\\d{2}$", message = "Ungültiges Monatformat")
    private String rolloverStartMonth;
}
