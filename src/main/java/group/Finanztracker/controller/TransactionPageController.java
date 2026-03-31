package group.Finanztracker.controller;

import group.Finanztracker.dto.CategoryResponse;
import group.Finanztracker.dto.TransactionRequest;
import group.Finanztracker.dto.TransactionResponse;
import group.Finanztracker.entity.TransactionType;
import group.Finanztracker.service.CategoryService;
import group.Finanztracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionPageController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final Clock clock;

    @GetMapping
    public String transactions(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                               @RequestParam(required = false) TransactionType type,
                               @RequestParam(required = false) Long categoryId,
                               Model model) {
        YearMonth selectedMonth = month != null ? month : YearMonth.now(clock);
        TransactionRequest request = TransactionRequest.builder()
                .date(LocalDate.now(clock))
                .type(TransactionType.EXPENSE)
                .build();
        populateTransactionPage(model, request, selectedMonth, type, categoryId);
        model.addAttribute("editing", false);
        return "transactions";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("transactionForm") TransactionRequest request,
                         BindingResult bindingResult,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                         @RequestParam(required = false) TransactionType type,
                         @RequestParam(required = false) Long categoryId,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        YearMonth selectedMonth = month != null ? month : YearMonth.now(clock);
        if (bindingResult.hasErrors()) {
            populateTransactionPage(model, request, selectedMonth, type, categoryId);
            model.addAttribute("editing", false);
            return "transactions";
        }
        transactionService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Transaktion wurde gespeichert.");
        return "redirect:/transactions?month=" + selectedMonth;
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        TransactionResponse transaction = transactionService.findById(id);
        TransactionRequest request = TransactionRequest.builder()
                .title(transaction.getTitle())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .categoryId(transaction.getCategoryId())
                .date(transaction.getDate())
                .note(transaction.getNote())
                .build();
        model.addAttribute("transactionId", id);
        model.addAttribute("transactionForm", request);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("transactionTypes", Arrays.asList(TransactionType.values()));
        model.addAttribute("editing", true);
        return "transaction-edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("transactionForm") TransactionRequest request,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("transactionId", id);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("transactionTypes", Arrays.asList(TransactionType.values()));
            model.addAttribute("editing", true);
            return "transaction-edit";
        }
        transactionService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Transaktion wurde aktualisiert.");
        return "redirect:/transactions?month=" + YearMonth.from(request.getDate());
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        transactionService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Transaktion wurde gelöscht.");
        return "redirect:/transactions";
    }

    private void populateTransactionPage(Model model,
                                         TransactionRequest request,
                                         YearMonth selectedMonth,
                                         TransactionType type,
                                         Long categoryId) {
        List<TransactionResponse> transactions = transactionService.findAllForMonth(selectedMonth).stream()
                .filter(transaction -> type == null || transaction.getType() == type)
                .filter(transaction -> categoryId == null || transaction.getCategoryId().equals(categoryId))
                .toList();
        List<CategoryResponse> categories = categoryService.findAll();
        model.addAttribute("transactions", transactions);
        model.addAttribute("categories", categories);
        model.addAttribute("transactionTypes", Arrays.asList(TransactionType.values()));
        model.addAttribute("transactionForm", request);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedCategoryId", categoryId);
    }
}
