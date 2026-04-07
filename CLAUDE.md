# CLAUDE.md — Finanztracker

## Code-Bereich

Sämtlicher Java-Code gehört ausschließlich in:

```
src/main/java/group/Finanztracker/
```

Keine neuen Top-Level-Packages anlegen. Unterpackages (`security/`) nur dort, wo sie bereits existieren.

## Paketstruktur

Die bestehende Schichtarchitektur ist einzuhalten:

| Package        | Zweck |
|----------------|-------|
| `config/`      | Spring-Konfigurationsklassen |
| `controller/`  | Spring MVC Controller (Thymeleaf-Seiten) |
| `dto/`         | Datentransferobjekte: `*Form` (UI-Eingabe), `*Request` (Service-Eingabe), `*Response` (Service-Ausgabe), `*ViewModel` (View-Zusammenstellung) |
| `entity/`      | JPA-Entitäten |
| `exception/`   | Eigene Exception-Klassen und Handler |
| `mapper/`      | Mapper zwischen Entity ↔ DTO |
| `repository/`  | Spring Data JPA Repositories |
| `service/`     | Geschäftslogik |

Templates liegen unter `src/main/resources/templates/`, CSS unter `src/main/resources/static/css/`.  
Datenbankmigrationen sind Flyway-Skripte unter `src/main/resources/db/migration/` (Namensschema: `V{n}__{beschreibung}.sql`).

## Tech-Stack

Nur die bereits eingebundenen Bibliotheken verwenden:

- Spring Boot (Web MVC, Data JPA, Security, Validation, Mail)
- Thymeleaf
- Flyway + PostgreSQL
- Lombok
- Docker Compose (lokale Entwicklung)

**Wird eine neue Bibliothek benötigt, erst fragen — keine eigenständigen `pom.xml`-Änderungen.**

## Konventionen

- Lombok-Annotationen (`@Getter`, `@Setter`, `@Builder`, `@RequiredArgsConstructor` usw.) statt manueller Boilerplate
- Validierung über Bean Validation (`@NotNull`, `@Size` usw.) auf DTOs
- Eigene Exceptions aus `exception/` werfen; nicht `RuntimeException` direkt verwenden
- Keine Kommentare für selbsterklärenden Code; nur kommentieren, wenn die Logik nicht offensichtlich ist
- Keine zusätzliche Fehlerbehandlung für Fälle, die im normalen Betrieb nicht auftreten können

## Tests

Tests nur nach Rücksprache schreiben. Kein Test-Code ohne explizite Aufforderung.

## Git

**Git wird NIEMALS von Claude ausgeführt.** Keinerlei Git-Befehle — auch nicht `git log`, `git diff`, `git status`, `git rev-parse` oder ähnliches. Git wird ausschließlich vom Nutzer selbst verwaltet. Bei Bedarf kann Claude einen sinnvollen Commit-Message-Vorschlag machen, aber nicht selbst committen. Für Code-Reviews wird der aktuelle Dateizustand direkt gelesen — niemals git.

## Weitere Hinweise für Claude

- Diese Datei wird kontinuierlich verbessert. Wenn im Gespräch etwas Sinnvolles ergänzt werden könnte (z. B. eine neue Konvention fällt auf), kurz nachfragen ob es aufgenommen werden soll.
- Vor Änderungen an bestehenden Dateien immer erst lesen.
- Keine spekulativen Abstraktionen oder Features, die nicht explizit angefragt wurden.
