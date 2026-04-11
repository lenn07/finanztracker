# Budget-Übertrag (Rollover) — Design Spec
**Datum:** 2026-04-11

## Zusammenfassung

Nutzer können einen monatlichen Budget-Übertrag aktivieren. Nicht verbrauchtes Budget wird in den Folge­monat übertragen; Über­schreitungen werden abgezogen. Der Übertrag akkumuliert sich über mehrere Monate ab einem konfigurierbaren Startmonat. Das effektive Budget wird immer auf mindestens 0 € gedeckelt. Das Feature ist pro Nutzer in den Einstellungen ein- und ausschaltbar.

---

## Datenbank

**Migration V7** — zwei neue Spalten in `budget_settings`:

| Spalte | Typ | Default | Nullable |
|---|---|---|---|
| `rollover_enabled` | boolean | `false` | nein |
| `rollover_start_month` | date | — | ja |

`rollover_start_month` speichert den ersten Tag des gewählten Monats (z.B. `2025-01-01` für Januar 2025). Der Wert bleibt erhalten wenn das Feature deaktiviert wird — nur `rollover_enabled` wird auf `false` gesetzt.

---

## Backend

### TotalBudget (Entity)
Zwei neue Felder:
- `rolloverEnabled` (boolean)
- `rolloverStartMonth` (LocalDate, nullable)

### Rollover-Berechnung (TotalBudgetService)
Neue Methode `calculateRollover(YearMonth currentMonth)`:

```
wenn rolloverEnabled = false → return 0
wenn rolloverStartMonth = null → return 0
wenn rolloverStartMonth >= currentMonth → return 0

rolloverAmount = Σ (budget − spent) für jeden Monat von rolloverStartMonth bis currentMonth − 1
effectiveBudget = max(0, baseBudget + rolloverAmount)
```

`rolloverAmount` kann negativ sein. `effectiveBudget` ist immer ≥ 0.

### MonthlyOverviewResponse (DTO)
Zwei neue Felder:
- `rolloverAmount` (BigDecimal) — kann negativ sein; 0 wenn Feature deaktiviert
- `effectiveBudget` (BigDecimal) — immer ≥ 0; gleich `totalBudget` wenn Feature deaktiviert

`remainingBudget` bleibt unverändert (rechnet weiterhin gegen `totalBudget`).

### Settings — POST /settings/rollover
Neues Formular-DTO `RolloverSettingsForm` mit:
- `rolloverEnabled` (boolean)
- `rolloverStartMonth` (String im Format `yyyy-MM`, aus `<input type="month">`)

`SettingsPageController` bekommt einen neuen POST-Handler, der `TotalBudgetService.saveRolloverSettings(...)` aufruft.

---

## Frontend

### Settings-Seite (`settings.html`)
Neue Card "Budget-Übertrag" unterhalb der bestehenden "Konto"-Card:

- Toggle (Checkbox als Switch): "Übertrag aktivieren"
- Monatsfeld (`<input type="month">`), nur sichtbar wenn Toggle aktiv: "Übertrag berechnen ab"
- Speichern-Button

### Dashboard (`dashboard.html`)
Unterhalb des bestehenden Budget-Blocks, nur wenn `rolloverAmount != 0` oder `rolloverEnabled = true`:

- "Übertrag: +70,00 €" — grün bei positivem, rot bei negativem Wert
- "Effektives Budget: 370,00 €" — fett

Wenn Rollover deaktiviert: keine zusätzlichen Zeilen.

### Budget-Seite (`budgets.html`)
Bleibt vollständig unverändert.

---

## Nicht im Scope

- Rollover für Kategorie-Budgets
- Manuelle Korrektur des Übertrags (Workaround: Startmonat auf aktuellen Monat setzen)
- Obergrenzen für den Übertrag
- Einfrieren von Monatsdifferenzen (Berechnung ist immer dynamisch)
