package group.Finanztracker.dto;

import java.math.BigDecimal;

public record CategoryMonthAmountPoint(String categoryName, int year, int month, BigDecimal amount) {}
