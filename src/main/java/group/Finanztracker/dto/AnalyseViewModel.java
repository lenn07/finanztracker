package group.Finanztracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyseViewModel {
    private List<String> trendLabels;
    private List<BigDecimal> trendExpenses;
    private List<BigDecimal> trendIncome;
    private List<String> categoryLabels;
    private List<BigDecimal> categoryData;
    private int selectedMonths;
    private List<String> categoryTrendNames;
    private List<List<BigDecimal>> categoryTrendData;
}
