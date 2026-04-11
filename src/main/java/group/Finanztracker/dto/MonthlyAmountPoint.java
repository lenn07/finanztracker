package group.Finanztracker.dto;

import java.math.BigDecimal;

public record MonthlyAmountPoint(int year, int month, BigDecimal amount) {}
