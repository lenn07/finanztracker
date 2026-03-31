# UI-Routen

## Web-Oberfläche
- `/` leitet auf das Dashboard weiter
- `/dashboard` zeigt die Monatsübersicht
- `/transactions` zeigt Liste, Filter und Formular für Transaktionen
- `/transactions/{id}/edit` bearbeitet eine Transaktion
- `/categories` zeigt Kategorieverwaltung
- `/categories/{id}/edit` bearbeitet eine Kategorie
- `/budgets` zeigt Gesamtbudget und Kategorie-Budgets
- `/budgets/category/{id}/edit` bearbeitet ein Kategorie-Budget

# API Endpoints

## Kategorien
- `/api/categories` (GET): Alle Kategorien abrufen
- `/api/categories` (POST): Neue Kategorie anlegen
- `/api/categories/{id}` (GET): Kategorie nach ID abrufen
- `/api/categories/{id}` (PUT): Kategorie aktualisieren
- `/api/categories/{id}` (DELETE): Kategorie löschen

Body Beispiel:
```json
{
  "name": "Lebensmittel"
}
```

## Transaktionen
- `/api/transactions` (GET): Alle Transaktionen abrufen
- `/api/transactions` (POST): Neue Transaktion anlegen
- `/api/transactions/category/{categoryId}` (GET): Transaktionen einer Kategorie abrufen
- `/api/transactions/type/{type}` (GET): Transaktionen nach Typ abrufen
- `/api/transactions/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` (GET): Zeitraum abrufen
- `/api/transactions/{id}` (GET): Transaktion nach ID abrufen
- `/api/transactions/{id}` (PUT): Transaktion aktualisieren
- `/api/transactions/{id}` (DELETE): Transaktion löschen
- `/api/transactions/sum/{categoryId}` (GET): Summe der Ausgaben einer Kategorie
- `/api/transactions/sum` (GET): Gesamtsumme aller Ausgaben

Body Beispiel:
```json
{
  "title": "Supermarkt Einkauf",
  "amount": 75.50,
  "type": "EXPENSE",
  "categoryId": 2,
  "date": "2026-03-30",
  "note": "Wocheneinkauf"
}
```

## Gesamtbudget
- `/api/budgets/total` (GET): Alle Gesamtbudgets abrufen
- `/api/budgets/total` (POST): Neues Gesamtbudget anlegen
- `/api/budgets/total/{id}` (GET): Gesamtbudget nach ID abrufen
- `/api/budgets/total/{id}` (PUT): Gesamtbudget aktualisieren
- `/api/budgets/total/{id}` (DELETE): Gesamtbudget löschen

Body Beispiel:
```json
{
  "totalMonthlyLimit": 1200.00
}
```

## Kategorie-Budgets
- `/api/budgets/category` (GET): Alle Kategorie-Budgets abrufen
- `/api/budgets/category` (POST): Neues Kategorie-Budget anlegen
- `/api/budgets/category/{id}` (GET): Kategorie-Budget nach ID abrufen
- `/api/budgets/category/{id}` (PUT): Kategorie-Budget aktualisieren
- `/api/budgets/category/{id}` (DELETE): Kategorie-Budget löschen

Body Beispiel:
```json
{
  "categoryId": 1,
  "monthlyLimit": 250.00
}
```

## Dashboard / Monatsübersicht
- `/api/dashboard/monthly?month=YYYY-MM` (GET): Monatsübersicht mit Gesamtbudget, Ausgaben, Restbudget, Einnahmen und Kategorieverbrauch
