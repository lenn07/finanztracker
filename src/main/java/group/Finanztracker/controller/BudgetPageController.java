package group.Finanztracker.controller;

import group.Finanztracker.dto.CategoryBudgetRequest;
import group.Finanztracker.dto.CategoryBudgetResponse;
import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.service.BudgetSettingsQueryService;
import group.Finanztracker.service.CategoryBudgetService;
import group.Finanztracker.service.CategoryService;
import group.Finanztracker.service.TotalBudgetService;
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

@Controller
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetPageController {

    private final TotalBudgetService totalBudgetService;
    private final CategoryBudgetService categoryBudgetService;
    private final BudgetSettingsQueryService budgetSettingsQueryService;
    private final CategoryService categoryService;

    @GetMapping
    public String budgets(Model model) {
        populateBudgetPage(model, new TotalBudgetRequest(), new CategoryBudgetRequest());
        totalBudgetService.getCurrentBudget()
                .ifPresent(budget -> model.addAttribute("totalBudgetForm",
                        TotalBudgetRequest.builder().totalMonthlyLimit(budget.getTotalMonthlyLimit()).build()));
        return "budgets";
    }

    @PostMapping("/total")
    public String saveTotalBudget(@Valid @ModelAttribute("totalBudgetForm") TotalBudgetRequest form,
                                  BindingResult bindingResult,
                                  @ModelAttribute("categoryBudgetForm") CategoryBudgetRequest categoryBudgetForm,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateBudgetPage(model, form, categoryBudgetForm);
            return "budgets";
        }
        totalBudgetService.saveOrUpdate(form);
        redirectAttributes.addFlashAttribute("successMessage", "Gesamtbudget wurde gespeichert.");
        return "redirect:/budgets";
    }

    @PostMapping("/category")
    public String createCategoryBudget(@Valid @ModelAttribute("categoryBudgetForm") CategoryBudgetRequest form,
                                       BindingResult bindingResult,
                                       @ModelAttribute("totalBudgetForm") TotalBudgetRequest totalBudgetForm,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            populateBudgetPage(model, totalBudgetForm, form);
            return "budgets";
        }
        try {
            categoryBudgetService.create(form);
        } catch (IllegalStateException ex) {
            bindingResult.rejectValue("percentage", "categoryBudget.invalid", ex.getMessage());
            populateBudgetPage(model, totalBudgetForm, form);
            return "budgets";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie-Budget wurde gespeichert.");
        return "redirect:/budgets";
    }

    @GetMapping("/category/{id}/edit")
    public String editCategoryBudget(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        if (!categoryBudgetService.hasUsableTotalBudget()) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Bitte zuerst ein Gesamtbudget anlegen, bevor Kategorie-Budgets bearbeitet werden können.");
            return "redirect:/budgets";
        }
        CategoryBudgetResponse categoryBudget = categoryBudgetService.getById(id);
        model.addAttribute("categoryBudgetId", id);
        model.addAttribute("categoryBudgetForm", CategoryBudgetRequest.builder()
                .categoryId(categoryBudget.getCategoryId())
                .percentage(categoryBudget.getPercentage())
                .build());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("totalMonthlyLimit", categoryBudget.getCalculatedMonthlyLimit());
        return "budget-category-edit";
    }

    @PostMapping("/category/{id}")
    public String updateCategoryBudget(@PathVariable Long id,
                                       @Valid @ModelAttribute("categoryBudgetForm") CategoryBudgetRequest form,
                                       BindingResult bindingResult,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryBudgetId", id);
            model.addAttribute("categories", categoryService.findAll());
            return "budget-category-edit";
        }
        try {
            categoryBudgetService.update(id, form);
        } catch (IllegalStateException ex) {
            bindingResult.rejectValue("percentage", "categoryBudget.invalid", ex.getMessage());
            model.addAttribute("categoryBudgetId", id);
            model.addAttribute("categories", categoryService.findAll());
            return "budget-category-edit";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie-Budget wurde aktualisiert.");
        return "redirect:/budgets";
    }

    @PostMapping("/category/{id}/delete")
    public String deleteCategoryBudget(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryBudgetService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie-Budget wurde gelöscht.");
        return "redirect:/budgets";
    }

    private void populateBudgetPage(Model model,
                                    TotalBudgetRequest totalBudgetForm,
                                    CategoryBudgetRequest categoryBudgetForm) {
        model.addAttribute("budgetSettings", budgetSettingsQueryService.getViewModel());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("totalBudgetForm", totalBudgetForm);
        model.addAttribute("categoryBudgetForm", categoryBudgetForm);
    }
}
