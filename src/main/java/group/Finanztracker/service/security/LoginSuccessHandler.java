package group.Finanztracker.service.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final CurrentUserService currentUserService;

    public LoginSuccessHandler(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
        setDefaultTargetUrl("/dashboard");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUserPrincipal securityUserPrincipal) {
            currentUserService.updateLastLogin(securityUserPrincipal.getId());
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
