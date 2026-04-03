# Finanztracker

Finanztracker ist eine Spring-Boot-Webanwendung zur persoenlichen Verwaltung von Einnahmen, Ausgaben und Monatsbudgets. Die App kombiniert servergerenderte Oberflaechen mit einer kleinen REST-API und enthaelt inzwischen auch Benutzerkonten mit Registrierung, Login und E-Mail-Verifikation.

## Funktionen
- Einnahmen und Ausgaben erfassen, bearbeiten und loeschen
- Wiederkehrende Abonnements mit monatlichem, vierteljaehrlichem oder jaehrlichem Intervall verwalten
- Kategorien anlegen, bearbeiten und loeschen
- Globales Monatsbudget definieren
- Kategorie-Budgets mit festen Monatslimits verwalten
- Monatsuebersicht mit Restbudget, Ausgaben, Einnahmen und Kategorieverbrauch
- Filter fuer Transaktionen nach Monat, Typ und Kategorie
- Registrierung und Login mit Spring Security
- E-Mail-Verifikation fuer neue Konten
- Benutzerbezogene Datenhaltung, damit jede Person nur ihre eigenen Daten sieht

Wichtig: Budgets werden aktuell nur gegen `EXPENSE`-Transaktionen gerechnet. Einnahmen werden gespeichert und angezeigt, reduzieren aber kein Budget.

## Tech-Stack
- Java 17
- Spring Boot 4
- Spring MVC
- Thymeleaf
- Spring Security
- Spring Data JPA
- Flyway
- PostgreSQL
- Spring Mail
- Lombok
- JUnit 5 / Mockito

## Projektstruktur
- `src/main/java/group/Finanztracker/controller`: Page- und REST-Controller
- `src/main/java/group/Finanztracker/service`: Fachlogik
- `src/main/java/group/Finanztracker/service/security`: Login-, Registrierungs- und Benutzerlogik
- `src/main/java/group/Finanztracker/repository`: Datenzugriff
- `src/main/java/group/Finanztracker/entity`: JPA-Entities
- `src/main/java/group/Finanztracker/dto`: Form-, Request- und Response-Modelle
- `src/main/java/group/Finanztracker/mapper`: Mapping zwischen DTOs und Entities
- `src/main/resources/templates`: Thymeleaf-Views
- `src/main/resources/static/css`: Styles
- `src/main/resources/db/migration`: Flyway-Migrationen
- `.agents/skills`: Projektlokale Codex-Skills fuer wiederkehrende Arbeitsablaeufe

## Voraussetzungen
- Java 17
- Docker oder eine lokal laufende PostgreSQL-Datenbank
- Optional: Ein SMTP-Konto fuer Verifikationsmails

## Lokale Konfiguration
Die Anwendung liest ihre Werte aktuell aus `src/main/resources/application.properties`.

Mindestens diese Eintraege muessen zu deiner lokalen Umgebung passen:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret

app.mail.from=deine-mail@example.com
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=deine-mail@example.com
spring.mail.password=app-password
```

Hinweise:
- Lege keine echten Zugangsdaten in die README oder in ein oeffentliches Repository.
- Fuer lokale Entwicklung sind Umgebungsvariablen oder eine nicht versionierte Konfigurationsdatei die bessere Wahl.
- Wenn du keine echten Mails versenden willst, kannst du spaeter auf ein Test-SMTP-Setup wie MailHog oder Mailtrap wechseln.

## Anwendung starten
### 1. Datenbank starten
Das Projekt enthaelt eine `compose.yaml` mit PostgreSQL:

```powershell
docker compose up -d
```

Die Standardwerte in der Compose-Datei sind:
- Datenbank: `mydatabase`
- Benutzer: `myuser`
- Passwort: `secret`
- Port: `5432`

### 2. Anwendung starten
```powershell
.\mvnw.cmd spring-boot:run
```

### 3. App oeffnen
- Web-UI: `http://localhost:8080`
- Login: `http://localhost:8080/auth/login`
- Registrierung: `http://localhost:8080/auth/register`

## Authentifizierung
- Nicht angemeldete Nutzer werden auf `/auth/login` geleitet.
- Neue Benutzer registrieren sich ueber `/auth/register`.
- Nach der Registrierung wird eine Verifikationsmail verschickt.
- Erst nach erfolgreicher Verifikation ist ein Login moeglich.
- Nach dem Login landet der Nutzer auf dem Dashboard.

## Wichtige Seiten
- `/dashboard` zeigt die Monatsuebersicht
- `/transactions` verwaltet Einnahmen und Ausgaben
- `/categories` verwaltet Kategorien
- `/subscriptions` verwaltet wiederkehrende Abonnements
- `/budgets` verwaltet Gesamtbudget und Kategorie-Budgets
- `/auth/login` zeigt die Anmeldung
- `/auth/register` zeigt die Registrierung
- `/auth/verify?token=...` verarbeitet die E-Mail-Verifikation

## REST-API
Neben der Weboberflaeche gibt es auch REST-Endpunkte fuer Kategorien, Transaktionen, Budgets und Monatsauswertungen.

Eine Uebersicht findest du in [`ENDPOINTS.md`](/c:/IT/spring%20boot/Finanztracker/ENDPOINTS.md).

## Datenmodell in einfachen Worten
- `Category`: frei benennbare Kategorie wie Lebensmittel oder Freizeit
- `Transaction`: einzelner Einnahme- oder Ausgabe-Eintrag mit Betrag, Datum und Kategorie
- `Subscription`: wiederkehrender Eintrag mit Intervall, Laufzeit und automatischer Transaktionserzeugung
- `TotalBudget`: globales Monatsbudget pro Benutzer
- `CategoryBudget`: Monatslimit fuer genau eine Kategorie
- `AppUser`: Benutzerkonto fuer Login und Datentrennung
- `EmailVerificationToken`: Token fuer die Freischaltung neuer Konten

Die Budgets werden derzeit nicht historisiert. Wenn ein Limit geaendert wird, gilt ab dann der neue Wert.

Abonnements erzeugen beim Oeffnen der App automatisch fehlende Transaktionen bis maximal zum aktuellen Monat. Bereits erzeugte Abo-Transaktionen werden ueber die Abo-Verknuepfung vor Dubletten geschuetzt.

## Tests
```powershell
.\mvnw.cmd test
```

Aktuell sind vor allem Unit-Tests fuer Geschaeftsregeln enthalten, zum Beispiel:
- Monatsuebersicht
- Kategorie-Regeln
- Basis-Test fuer die Anwendungsklasse

## Nuetzliche Hinweise fuer Git
- `.mvn/` gehoert normalerweise ins Repository, weil dort die Maven-Wrapper-Konfiguration liegt.
- `.m2/` ist ein lokaler Maven-Cache und sollte nicht mitgepusht werden.
- `target/` sollte ebenfalls nicht versioniert werden.

## Codex-Skills
- Der projektlokale Skill `implement-feature` liegt unter `.agents/skills/implement-feature`.
- Er fuehrt Feature-Arbeit entlang dieses Ablaufs: betroffene Komponenten finden, Implementierungsplan ableiten, bei echten Unklarheiten rueckfragen, Aenderungen klein halten, README pruefen und eine kurze Summary liefern.
- Tests werden durch diesen Skill nicht ungefragt ergaenzt oder ausgefuehrt.
- Der projektlokale Skill `idea-finder` liegt unter `.agents/skills/idea-finder`.
- Er hilft dabei, Verbesserungs- und Erweiterungsideen fuer das Projekt zu sammeln, zu bewerten und zu priorisieren, ohne Code oder Dateien zu aendern.
