package group.Finanztracker.controller.security;

import group.Finanztracker.dto.security.RegistrationForm;
import group.Finanztracker.dto.security.ResendVerificationForm;
import group.Finanztracker.service.security.EmailVerificationService;
import group.Finanztracker.service.security.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthPageController {

    private final RegistrationService registrationService;
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("resendVerificationForm")) {
            model.addAttribute("resendVerificationForm", new ResendVerificationForm());
        }
        return "security/login/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        return "security/login/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (!form.passwordsMatch()) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Die Passwörter stimmen nicht überein.");
        }
        if (bindingResult.hasErrors()) {
            return "security/login/register";
        }
        try {
            registrationService.register(form, currentBaseUrl());
        } catch (IllegalStateException ex) {
            bindingResult.rejectValue("email", "register.failed", ex.getMessage());
            return "security/login/register";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Dein Konto wurde angelegt. Bitte bestätige jetzt deine E-Mail-Adresse.");
        redirectAttributes.addFlashAttribute("resendVerificationForm", ResendVerificationForm.builder().email(form.getEmail()).build());
        return "redirect:/auth/login";
    }

    @PostMapping("/resend-verification")
    public String resendVerification(@Valid @ModelAttribute("resendVerificationForm") ResendVerificationForm form,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.resendVerificationForm", bindingResult);
            redirectAttributes.addFlashAttribute("resendVerificationForm", form);
            return "redirect:/auth/login";
        }
        try {
            registrationService.resendVerificationMail(form.getEmail(), currentBaseUrl());
            redirectAttributes.addFlashAttribute("successMessage", "Wir haben dir einen neuen Bestätigungslink geschickt.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("dangerMessage", ex.getMessage());
        }
        redirectAttributes.addFlashAttribute("resendVerificationForm", form);
        return "redirect:/auth/login";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token,
                         Model model) {
        try {
            emailVerificationService.verifyToken(token);
            model.addAttribute("verificationSucceeded", true);
            model.addAttribute("verificationMessage", "Deine E-Mail-Adresse wurde bestätigt. Du kannst dich jetzt anmelden.");
        } catch (IllegalStateException ex) {
            model.addAttribute("verificationSucceeded", false);
            model.addAttribute("verificationMessage", ex.getMessage());
        }
        return "security/login/verify-result";
    }

    private String currentBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
