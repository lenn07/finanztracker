package group.Finanztracker.entity;

public enum SubscriptionInterval {
    MONTHLY(1, "Monatlich"),
    QUARTERLY(3, "Vierteljaehrlich"),
    YEARLY(12, "Jaehrlich");

    private final int monthsStep;
    private final String label;

    SubscriptionInterval(int monthsStep, String label) {
        this.monthsStep = monthsStep;
        this.label = label;
    }

    public int getMonthsStep() {
        return monthsStep;
    }

    public String getLabel() {
        return label;
    }
}
