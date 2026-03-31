---
name: spring-boot-review-and-fix
description: Pruefe und verbessere ein Spring-Boot-Projekt auf saubere Schichten, uebliche Spring-Boot-Konventionen und klare Verantwortlichkeiten. Use when Codex should review a Spring Boot project for layered architecture, controller/service/repository separation, DTO usage, naming consistency, validation, exception handling, mapper boundaries, and package responsibilities, and directly fix safe issues with small refactorings.
---

# Spring Boot Review And Fix

Pruefe den Code und behebe sichere, lokale Verstoesse direkt.

Arbeite in dieser Reihenfolge:

1. Untersuche Controller, Services, Repositories, DTOs, Entities, Mapper und Exception-Handling.
2. Ordne jede Klasse ihrer tatsaechlichen Verantwortung zu.
3. Markiere Verstoesse gegen typische Spring-Boot-Konventionen.
4. Behebe sichere, kleine Refactorings direkt.
5. Stoppe bei groesseren Architekturentscheidungen und benenne die Trade-offs.

Nutze fuer Reviews und Refactorings diese Regeln:

- Controller bleiben schlank und enthalten nur HTTP-nahe Logik.
- Business-Logik gehoert in Services.
- Repositories dienen nur Persistence und Query-Zugriffen.
- Request-DTOs werden nur fuer Eingaben verwendet.
- Response-DTOs werden nur fuer Ausgaben verwendet.
- Entities werden nicht direkt ueber API-Grenzen geleakt, wenn das Projekt bereits DTOs nutzt.
- Mapper konvertieren nur und enthalten keine Business-Logik.
- Validation sitzt an der Eingabegrenze.
- Fehlerbehandlung ist konsistent und moeglichst zentral.
- Benennungen und Packages passen zur Verantwortung der Klasse.

Pruefe insbesondere diese Anti-Patterns:

- Controller mit Datenbankzugriff oder Rechenlogik
- Repository mit Business-Logik
- Service mit unnoetigen HTTP-Details
- DTOs mit gemischter Input- und Output-Rolle
- Entities mit API-spezifischem Verhalten
- Mapper mit Validierung, Berechnung oder Seiteneffekten
- irrefuehrende Klassennamen oder Package-Namen

Erlaube nur diese direkten Anpassungen ohne Rueckfrage:

- Benennungen klarziehen, wenn die Aenderung lokal und eindeutig ist
- kleine Logikverschiebungen zwischen Controller, Service und Mapper
- Validation an die passende Eingabegrenze verschieben
- inkonsistente DTO-Nutzung bereinigen, wenn der API-Vertrag gleich bleibt
- kleine Aufraeumarbeiten im Exception-Handling

Stoppe und erklaere das Risiko statt direkt umzubauen, wenn:

- sich API-Vertraege aendern wuerden
- Datenbankmodell oder Migrationen betroffen sind
- mehrere Klassen gleichzeitig stark umgebaut werden muessen
- unklar ist, ob bestehendes Verhalten absichtlich so gebaut wurde

Wenn du antwortest:

- Nenne zuerst Findings.
- Sage explizit, welche sicheren Verstoesse du direkt angepasst hast.
- Referenziere konkrete Dateien.
- Erwaehne offene groessere Punkte getrennt von bereits umgesetzten Fixes.

Wenn du unsicher bist, lies [references/checklist.md](references/checklist.md).
