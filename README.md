# Finanztracker

Finanztracker ist eine Spring-Boot-Webanwendung zur persoenlichen Verwaltung von Einnahmen, Ausgaben und Monatsbudgets. Die App kombiniert servergerenderte Oberflaechen mit einer kleinen REST-API und enthaelt Benutzerkonten mit Registrierung, Login und E-Mail-Verifikation.

## Funktionen
- Einnahmen und Ausgaben erfassen, bearbeiten und loeschen
- Wiederkehrende Abonnements mit monatlichem, vierteljaehrlichem oder jaehrlichem Intervall verwalten
- Kategorien anlegen, bearbeiten und loeschen
- Globales Monatsbudget definieren, optional mit Uebertrag des Restbudgets in den Folgemonat
- Kategorie-Budgets mit festen Monatslimits verwalten
- Monatsuebersicht mit Restbudget, Ausgaben, Einnahmen und Kategorieverbrauch
- Filter fuer Transaktionen nach Monat, Typ und Kategorie
- Analyse-Seite mit monatlicher Einnahmen-/Ausgaben-Entwicklung und Kategorie-Diagrammen
- Registrierung und Login mit Spring Security
- E-Mail-Verifikation fuer neue Konten
- Benutzerbezogene Datenhaltung, damit jede Person nur ihre eigenen Daten sieht
- Einstellungsseite mit Option zum Loeschen des eigenen Kontos

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

## Lokale Konfiguration
Die Anwendung liest ihre Laufzeitwerte ueber Umgebungsvariablen. Fuer lokale Entwicklung importiert Spring Boot automatisch `.env` und `.env.local` aus dem Projektverzeichnis. Als Vorlage dient [`.env.example`](/c:/IT/spring%20boot/Finanztracker/.env.example).

Wichtige Variablen:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/mydatabase
SPRING_DATASOURCE_USERNAME=myuser
SPRING_DATASOURCE_PASSWORD=secret

APP_MAIL_REQUIRED=false
APP_MAIL_FROM=deine-mail@example.com
APP_PUBLIC_BASE_URL=http://localhost:8080
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=deine-mail@example.com
SPRING_MAIL_PASSWORD=app-password
```

Hinweise:
- `.env` und `.env.local` sind fuer lokale Geheimnisse gedacht und sollten nicht versioniert werden.
- Das Format ist bewusst Java-Properties-kompatibel: `KEY=value`.
- Wenn `APP_MAIL_REQUIRED=false` ist und keine SMTP-Daten vorhanden sind, schreibt die Anwendung den Verifikationslink nur ins Log.

## Deployment-Hinweise
Fuer Produktion sind drei Dinge wichtig:

1. Produktions-Defaults:
   SQL-Logging ist standardmaessig aus und Thymeleaf-Caching standardmaessig an.
2. Verifikationslinks:
   Mit `APP_PUBLIC_BASE_URL` kannst du die oeffentliche URL der Anwendung fest vorgeben, zum Beispiel `https://deine-domain.tld`.
3. Mailversand:
   Mit `APP_MAIL_REQUIRED=true` erzwingst du, dass SMTP wirklich konfiguriert ist. Fehlen Werte, kommt eine kontrollierte Fachfehlermeldung statt eines unklaren Mail-Stacktraces.

Empfohlene Variablen fuer Deployment:

```properties
PORT=8080
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<datenbank>
SPRING_DATASOURCE_USERNAME=<benutzer>
SPRING_DATASOURCE_PASSWORD=<passwort>

APP_MAIL_REQUIRED=true
APP_MAIL_FROM=no-reply@deine-domain.tld
APP_PUBLIC_BASE_URL=https://deine-domain.tld
SPRING_MAIL_HOST=<smtp-host>
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=<smtp-benutzer>
SPRING_MAIL_PASSWORD=<smtp-passwort>
```

## Flyway
Wenn deine Datenbank bereits eine alte, inzwischen geaenderte Migration gespeichert hat, ist das kein Problem der Anwendungskonfiguration, sondern der Flyway-Historie. In dem Fall solltest du die Migration nicht per Code automatisch reparieren lassen, sondern bewusst ausserhalb des normalen App-Starts vorgehen:

1. Urspruengliche Migration wiederherstellen und neue Migration anlegen, oder
2. die Datenbank gezielt per Flyway `repair` bereinigen, wenn du sicher bist, dass die aktuelle Migration fachlich korrekt ist.

Der Grund dafuer: Eine automatische Reparatur beim normalen Start waere fuer echte Deployments zu riskant.

## Starten
### Datenbank
```powershell
docker compose up --build
```

Damit starten lokal sowohl PostgreSQL als auch die Spring-Boot-App im Container.

Fuer den lokalen Compose-Betrieb gilt:
- Die App liest zusaetzliche Werte wie Mail-Konfiguration aus `.env`.
- Die Datenbankverbindung wird im App-Container automatisch auf den Compose-Service `postgres` umgebogen.
- Die PostgreSQL-Daten liegen persistent im Docker-Volume `pgdata`.
- Die App wartet ueber `depends_on` und den Postgres-Healthcheck, bis die Datenbank antwortet.

Die App ist danach unter `http://localhost:8080` erreichbar.

## Tests
```powershell
.\mvnw.cmd test
```

Tests habe ich nicht erstellt und nicht ausgefuehrt.
