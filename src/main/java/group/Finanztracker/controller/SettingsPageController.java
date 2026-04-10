package group.Finanztracker.controller;

import group.Finanztracker.service.AccountService;
import group.Finanztracker.service.security.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsPageController {

    private final AccountService accountService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public String settingsPage() {
        return "settings";
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
