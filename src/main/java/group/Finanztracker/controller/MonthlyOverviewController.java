package group.Finanztracker.controller;

import group.Finanztracker.dto.MonthlyOverviewResponse;
import group.Finanztracker.service.MonthlyOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class MonthlyOverviewController {

    private final MonthlyOverviewService monthlyOverviewService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyOverviewResponse> getMonthlyOverview(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        YearMonth selectedMonth = month != null ? month : YearMonth.now();
        return ResponseEntity.ok(monthlyOverviewService.getOverview(selectedMonth));
    }
}
