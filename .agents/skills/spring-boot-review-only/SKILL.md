---
name: spring-boot-review-only
description: Pruefe ein Spring-Boot-Projekt auf saubere Schichten, uebliche Spring-Boot-Konventionen und klare Verantwortlichkeiten, ohne Code zu aendern. Use when Codex should perform a review-only pass for layered architecture, controller/service/repository separation, DTO usage, naming consistency, validation, exception handling, mapper boundaries, and package responsibilities, and report findings only.
---

# Spring Boot Review Only

Pruefe den Code, nimm aber keine inhaltlichen Codeaenderungen vor.

Arbeite in dieser Reihenfolge:

1. Untersuche Controller, Services, Repositories, DTOs, Entities, Mapper und Exception-Handling.
2. Ordne jede Klasse ihrer tatsaechlichen Verantwortung zu.
3. Markiere Verstoesse gegen typische Spring-Boot-Konventionen.
4. Liefere Findings mit konkreten Dateien und klarer Begruendung.

Bewerte den Code anhand dieser Regeln:

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

Wenn du antwortest:

- Liefere zuerst Findings, geordnet nach Schweregrad.
- Referenziere konkrete Dateien.
- Sage explizit, dass dies ein Review-only-Durchlauf ohne Codeaenderung ist.
- Gib nur dann Verbesserungsvorschlaege, wenn sie aus den Findings folgen.

Wenn du unsicher bist, lies [references/checklist.md](references/checklist.md).
