


# API Endpoints – Stichpunkte & Funktion

## Kategorien
- /api/categories (GET): Alle Kategorien abrufen
- /api/categories (POST): Neue Kategorie anlegen
	**Body:**
	```json
	{
		"name": "Lebensmittel"
	}
	```
- /api/categories/{id} (GET): Kategorie nach ID abrufen
- /api/categories/{id} (PUT): Kategorie aktualisieren
	**Body:**
	```json
	{
		"name": "Freizeit"
	}
	```
- /api/categories/{id} (DELETE): Kategorie löschen

## Transaktionen
- /api/transactions (GET): Alle Transaktionen abrufen
- /api/transactions (POST): Neue Transaktion anlegen
	**Body:**
	```json
	{
		"title": "Gehalt März",
		"amount": 2500.00,
		"type": "INCOME",
		"categoryId": 1,
		"date": "2024-03-31",
		"note": "Monatsgehalt"
	}
	```
- /api/transactions/category/{categoryId} (GET): Transaktionen einer Kategorie abrufen
- /api/transactions/type/{type} (GET): Transaktionen nach Typ (INCOME/EXPENSE) abrufen
- /api/transactions/date-range?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD (GET): Transaktionen im Zeitraum abrufen
- /api/transactions/{id} (GET): Transaktion nach ID abrufen
- /api/transactions/{id} (PUT): Transaktion aktualisieren
	**Body:**
	```json
	{
		"title": "Supermarkt Einkauf",
		"amount": 75.50,
		"type": "EXPENSE",
		"categoryId": 2,
		"date": "2024-03-30",
		"note": "Wocheneinkauf"
	}
	```
- /api/transactions/{id} (DELETE): Transaktion löschen
- /api/transactions/sum/{categoryId} (GET): Summe der Transaktionen einer Kategorie
- /api/transactions/sum (GET): Gesamtsumme aller Transaktionen

## Gesamtbudget (Total Budget)
- /api/budgets/total (GET): Alle Gesamtbudgets abrufen
- /api/budgets/total (POST): Neues Gesamtbudget anlegen
	**Body:**
	```json
	{
		"totalMonthlyLimit": 1000.00
	}
	```
- /api/budgets/total/{id} (GET): Gesamtbudget nach ID abrufen
- /api/budgets/total/{id} (PUT): Gesamtbudget aktualisieren
	**Body:**
	```json
	{
		"totalMonthlyLimit": 1200.00
	}
	```
- /api/budgets/total/{id} (DELETE): Gesamtbudget löschen

## Kategorie-Budget (Category Budget)
- /api/budgets/category (GET): Alle Kategorie-Budgets abrufen
- /api/budgets/category (POST): Neues Kategorie-Budget anlegen
	**Body:**
	```json
	{
		"categoryId": 1,
		"monthlyLimit": 200.00
	}
	```
- /api/budgets/category/{id} (GET): Kategorie-Budget nach ID abrufen
- /api/budgets/category/{id} (PUT): Kategorie-Budget aktualisieren
	**Body:**
	```json
	{
		"categoryId": 1,
		"monthlyLimit": 250.00
	}
	```
- /api/budgets/category/{id} (DELETE): Kategorie-Budget löschen
