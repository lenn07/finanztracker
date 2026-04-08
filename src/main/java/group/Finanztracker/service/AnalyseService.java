package group.Finanztracker.service;

import group.Finanztracker.dto.AnalyseViewModel;
import group.Finanztracker.entity.TransactionType;
import group.Finanztracker.repository.TransactionRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyseService {

    private static final DateTimeFormatter LABEL_FORMATTER =
            DateTimeFormatter.ofPattern("MMM yy", Locale.GERMAN);

    private final TransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public AnalyseViewModel getAnalyse(int months, YearMonth now) {
        Long userId = currentUserService.getCurrentUserId();
        YearMonth start = now.minusMonths(months - 1);
        LocalDate startDate = start.atDay(1);
        LocalDate endDate = now.atEndOfMonth();

        Map<YearMonth, BigDecimal> expenseMap = toYearMonthMap(
                transactionRepository.sumAmountGroupedByMonth(TransactionType.EXPENSE, startDate, endDate, userId));
        Map<YearMonth, BigDecimal> incomeMap = toYearMonthMap(
                transactionRepository.sumAmountGroupedByMonth(TransactionType.INCOME, startDate, endDate, userId));

        List<String> trendLabels = new ArrayList<>();
        List<BigDecimal> trendExpenses = new ArrayList<>();
        List<BigDecimal> trendIncome = new ArrayList<>();

        for (YearMonth ym = start; !ym.isAfter(now); ym = ym.plusMonths(1)) {
            trendLabels.add(ym.format(LABEL_FORMATTER));
            trendExpenses.add(expenseMap.getOrDefault(ym, BigDecimal.ZERO));
            trendIncome.add(incomeMap.getOrDefault(ym, BigDecimal.ZERO));
        }

        List<Object[]> categoryRows =
                transactionRepository.sumExpensesGroupedByCategory(startDate, endDate, userId);

        List<String> categoryLabels = new ArrayList<>();
        List<BigDecimal> categoryData = new ArrayList<>();

        for (Object[] row : categoryRows) {
            BigDecimal amount = (BigDecimal) row[1];
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                categoryLabels.add((String) row[0]);
                categoryData.add(amount);
            }
        }

        return AnalyseViewModel.builder()
                .trendLabels(trendLabels)
                .trendExpenses(trendExpenses)
                .trendIncome(trendIncome)
                .categoryLabels(categoryLabels)
                .categoryData(categoryData)
                .selectedMonths(months)
                .build();
    }

    private Map<YearMonth, BigDecimal> toYearMonthMap(List<Object[]> rows) {
        Map<YearMonth, BigDecimal> map = new HashMap<>();
        for (Object[] row : rows) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            map.put(YearMonth.of(year, month), (BigDecimal) row[2]);
        }
        return map;
    }
}
