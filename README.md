# Finanztracker

Ein persönlicher Finanztracker auf Basis von Spring Boot. Die erste Version ist bewusst als Single-User-App gedacht, damit du an einem echten Projekt lernst, wie Backend, Datenbank, Validierung, MVC und server-renderte Oberflächen zusammenspielen.

## Was die App in V1 kann
- Einnahmen und Ausgaben erfassen
- Eigene Kategorien anlegen, bearbeiten und löschen
- Ein globales Monatsbudget festlegen
- Pro Kategorie ein festes Monatslimit speichern
- Eine Monatsübersicht mit Restbudget und Kategorieverbrauch anzeigen

Wichtig: In Version 1 werden Budgets nur gegen `EXPENSE`-Transaktionen gerechnet. Einnahmen werden gespeichert und angezeigt, verändern aber nicht das Restbudget.

## Tech-Stack
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Flyway
- PostgreSQL
- Thymeleaf
- Lombok
- JUnit 5 / Mockito

## Projekt starten
### 1. PostgreSQL starten
```powershell
docker compose up -d
```

### 2. Anwendung starten
```powershell
.\mvnw.cmd spring-boot:run
```

### 3. App öffnen
- Web-UI: `http://localhost:8080`
- REST-API: `http://localhost:8080/api/...`

## Wichtige Seiten
- `/dashboard` zeigt die Monatsübersicht
- `/transactions` verwaltet Einnahmen und Ausgaben
- `/categories` verwaltet Kategorien
- `/budgets` verwaltet Gesamtbudget und Kategorie-Budgets

## Datenmodell in einfachen Worten
- `Category`: frei benennbare Kategorie wie Lebensmittel oder Freizeit
- `Transaction`: einzelner Einnahme- oder Ausgabe-Eintrag mit Betrag, Datum und Kategorie
- `TotalBudget`: globales Monatsbudget
- `CategoryBudget`: Monatslimit für genau eine Kategorie

Die Budgets werden nicht historisiert. Wenn du ein Limit änderst, gilt ab dann der neue Wert.

## Wie der Code aufgebaut ist
- `controller`: HTTP-Requests und Seitensteuerung
- `service`: Geschäftslogik
- `repository`: Datenbankzugriff
- `entity`: JPA-Datenmodelle
- `dto`: Daten für API, UI und Formulare
- `mapper`: Wandlung zwischen Entity und DTO
- `db/migration`: Flyway-Migrationen

## Lernfragen
- Warum gibt es DTOs zusätzlich zu Entities?
- Welche Logik gehört in Controller, welche in Services?
- Warum wird die Monatsübersicht im Backend berechnet und nicht im Template?
- Wieso braucht man Flyway-Migrationen statt `ddl-auto=update`?
- Warum werden nur Ausgaben gegen Budgets gerechnet?

## Tests
```powershell
.\mvnw.cmd test
```

Aktuell sind vor allem Unit-Tests für Geschäftsregeln enthalten:
- Monatsübersicht
- Kategorie-Regeln
- Basis-Test für die Anwendungsklasse
