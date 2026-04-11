package group.Finanztracker.controller;

import group.Finanztracker.dto.RolloverSettingsForm;
import group.Finanztracker.service.AccountService;
import group.Finanztracker.service.TotalBudgetService;
import group.Finanztracker.service.security.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsPageController {

    private final AccountService accountService;
    private final CurrentUserService currentUserService;
    private final TotalBudgetService totalBudgetService;

    @GetMapping
    public String settingsPage(Model model) {
        totalBudgetService.getRolloverSettings().ifPresentOrElse(
                form -> {
                    model.addAttribute("rolloverSettings", form);
                    model.addAttribute("budgetExists", true);
                },
                () -> model.addAttribute("budgetExists", false)
        );
        return "settings";
    }

    @PostMapping("/rollover")
    public String saveRolloverSettings(@ModelAttribute RolloverSettingsForm form) {
        totalBudgetService.saveRolloverSettings(form);
        return "redirect:/settings?rolloverSaved=true";
    }

    @PostMapping("/delete")
    public String deleteAccount(HttpSession session) {
        Long userId = currentUserService.getCurrentUserId();
        accountService.deleteAccount(userId);
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?accountDeleted=true";
    }
}
