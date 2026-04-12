package group.Finanztracker.controller;

import group.Finanztracker.dto.SubscriptionRequest;
import group.Finanztracker.dto.SubscriptionResponse;
import group.Finanztracker.entity.SubscriptionInterval;
import group.Finanztracker.entity.TransactionType;
import group.Finanztracker.service.CategoryService;
import group.Finanztracker.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

@Controller
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionPageController {

    private final SubscriptionService subscriptionService;
    private final CategoryService categoryService;
    private final Clock clock;

    @GetMapping
    public String subscriptions(Model model) {
        subscriptionService.synchronizeTransactionsUpTo(YearMonth.now(clock));
        populateSubscriptionPage(model, SubscriptionRequest.builder()
                .type(TransactionType.EXPENSE)
                .interval(SubscriptionInterval.MONTHLY)
                .startDate(LocalDate.now(clock))
                .active(true)
                .build());
        return "subscriptions";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("subscriptionForm") SubscriptionRequest form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateSubscriptionPage(model, form);
            return "subscriptions";
        }
        subscriptionService.create(form);
        redirectAttributes.addFlashAttribute("successMessage", "Abonnement wurde gespeichert.");
        return "redirect:/subscriptions";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        SubscriptionResponse subscription = subscriptionService.findById(id);
        model.addAttribute("subscriptionId", id);
        model.addAttribute("subscriptionForm", SubscriptionRequest.builder()
                .title(subscription.getTitle())
                .amount(subscription.getAmount())
                .type(subscription.getType())
                .categoryId(subscription.getCategoryId())
                .interval(subscription.getInterval())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .note(subscription.getNote())
                .active(subscription.isActive())
                .build());
        populateSharedFormData(model);
        return "subscription-edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("subscriptionForm") SubscriptionRequest form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("subscriptionId", id);
            populateSharedFormData(model);
            return "subscription-edit";
        }
        subscriptionService.update(id, form);
        redirectAttributes.addFlashAttribute("successMessage", "Abonnement wurde aktualisiert.");
        return "redirect:/subscriptions";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        subscriptionService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Abonnement wurde geloescht.");
        return "redirect:/subscriptions";
    }

    private void populateSubscriptionPage(Model model, SubscriptionRequest form) {
        model.addAttribute("subscriptions", subscriptionService.findAll());
        model.addAttribute("subscriptionForm", form);
        populateSharedFormData(model);
    }

    private void populateSharedFormData(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("transactionTypes", Arrays.asList(TransactionType.values()));
        model.addAttribute("subscriptionIntervals", Arrays.asList(SubscriptionInterval.values()));
    }

}
