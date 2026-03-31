---
name: spring-boot-standards-reviewer
description: Prüfe und verbessere Spring-Boot-Backend-Code auf saubere Schichten, übliche Spring-Boot-Konventionen und klare Verantwortlichkeiten. Use when Codex should review or refactor a Spring Boot project for layered architecture, controller/service/repository separation, DTO usage, naming consistency, validation, exception handling, mapper boundaries, and clean package responsibilities.
---

# Spring Boot Standards Reviewer

Prüfe zuerst die bestehende Paket- und Schichtenstruktur, bevor du Änderungen vorschlägst oder umsetzt.

Arbeite in dieser Reihenfolge:

1. Untersuche Controller, Services, Repositories, DTOs, Entities, Mapper und Exception-Handling.
2. Ordne jede Klasse ihrer tatsächlichen Verantwortung zu.
3. Markiere Verstöße gegen typische Spring-Boot-Konventionen.
4. Behebe saubere, risikoarme Verstöße direkt.
5. Stoppe und erkläre Trade-offs, wenn eine größere Architekturänderung nötig wäre.

Nutze für Reviews und Refactorings diese Regeln:

- Halte Controller schlank. Controller koordinieren HTTP, Request-Mapping, Response-Codes und DTO-Grenzen.
- Verschiebe Business-Logik in Services.
- Halte Repositories rein für Persistence-Zugriffe und Query-Methoden.
- Nutze Request-DTOs nur für Eingaben.
- Nutze Response-DTOs nur für Ausgaben.
- Gib Entities nicht direkt über API-Grenzen zurück, sofern das Projekt bereits DTOs nutzt.
- Nutze Mapper nur für Umwandlungen, nicht für Business-Logik.
- Lege Validierung an Eingabegrenzen ab, typischerweise in Request-DTOs oder Controller-Methoden.
- Halte Exception-Handling konsistent und zentral, sofern bereits globale Fehlerbehandlung vorhanden ist.
- Halte Benennungen eindeutig: `*Controller`, `*Service`, `*Repository`, `*Request`, `*Response`, `*Mapper`, `*Exception`.
- Prüfe, ob Package-Namen und Klassennamen mit ihrer Rolle übereinstimmen.

Prüfe insbesondere diese Anti-Patterns:

- Controller mit Datenbankzugriff oder Rechenlogik
- Repository mit Business-Logik
- Service, der HTTP-spezifische Details kennt, obwohl das vermeidbar ist
- DTOs, die gleichzeitig Input- und Output-Rolle mischen
- Entities mit API-spezifischem Verhalten
- Mapper mit Validierung, Berechnung oder Seiteneffekten
- inkonsistente oder irreführende Klassennamen
- mehrere konkurrierende Wege für dieselbe Verantwortung

Wenn du Änderungen umsetzt:

- Bevorzuge kleine, lokale Refactorings.
- Bewahre das bestehende API-Verhalten, sofern der Nutzer nichts anderes verlangt.
- Ergänze oder aktualisiere Tests, wenn vorhandene Tests betroffen sind oder ein Risiko entsteht.
- Halte Doku-Dateien wie Endpunktübersichten synchron, wenn API-Struktur oder Namensgebung sichtbar betroffen ist.

Wenn du eine Review-Antwort schreibst:

- Liste Findings nach Schweregrad.
- Benenne konkrete Dateien und den Verstoß gegen die Schichten- oder Spring-Boot-Konvention.
- Sage explizit, ob du nur geprüft oder auch angepasst hast.

Wenn du unsicher bist, lies [references/checklist.md](references/checklist.md). Verwende die Checkliste als Standardmaßstab, nicht als starres Dogma.
