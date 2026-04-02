package group.Finanztracker.controller.security;

import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class SecurityPageModelAdvice {

    private final CurrentUserService currentUserService;

    @ModelAttribute("currentUserEmail")
    public String currentUserEmail() {
        return currentUserService.getCurrentUserEmail();
    }

    @ModelAttribute("userLoggedIn")
    public boolean userLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
