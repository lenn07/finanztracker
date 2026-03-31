package group.Finanztracker.controller;

import group.Finanztracker.service.MonthlyOverviewService;
import group.Finanztracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Clock;
import java.time.YearMonth;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardPageController {

    private final MonthlyOverviewService monthlyOverviewService;
    private final TransactionService transactionService;
    private final Clock clock;

    @GetMapping
    public String dashboard(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
                            Model model) {
        YearMonth selectedMonth = month != null ? month : YearMonth.now(clock);
        model.addAttribute("overview", monthlyOverviewService.getOverview(selectedMonth));
        model.addAttribute("transactions", transactionService.findAllForMonth(selectedMonth));
        model.addAttribute("selectedMonth", selectedMonth);
        return "dashboard";
    }
}
