package group.Finanztracker.controller;

import group.Finanztracker.service.AnalyseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Clock;
import java.time.YearMonth;

@Controller
@RequestMapping("/analyse")
@RequiredArgsConstructor
public class AnalysePageController {

    private final AnalyseService analyseService;
    private final Clock clock;

    @GetMapping
    public String analyse(@RequestParam(defaultValue = "6") int months, Model model) {
        int selectedMonths = (months == 12) ? 12 : 6;
        model.addAttribute("vm", analyseService.getAnalyse(selectedMonths, YearMonth.now(clock)));
        return "analyse";
    }
}
