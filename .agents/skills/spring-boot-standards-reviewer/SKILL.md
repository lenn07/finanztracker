---
name: spring-boot-standards-reviewer
description: Pruefe und verbessere Spring-Boot-Backend-Code auf saubere Schichten, uebliche Spring-Boot-Konventionen und klare Verantwortlichkeiten. Use when Codex should review or refactor a Spring Boot project for layered architecture, controller/service/repository separation, DTO usage, naming consistency, validation, exception handling, mapper boundaries, and clean package responsibilities.
---

# Spring Boot Standards Reviewer

Pruefe zuerst die bestehende Paket- und Schichtenstruktur, bevor du Aenderungen vorschlaegst oder umsetzt.

Arbeite immer in einem von zwei Modi:

- `Bericht`: Analysiere Schichten, Konventionen und Risiken und schreibe nur einen Bericht. Nimm keine Dateiaenderungen vor.
- `Direkte Aenderung`: Analysiere wie bisher und behebe saubere, risikoarme Verstoesse direkt.

Bestimme den Modus so:

- Wenn der Nutzer explizit `Bericht`, `nur Bericht`, `nur pruefen`, `Review` oder aehnlich verlangt, arbeite im Modus `Bericht`.
- Wenn der Nutzer explizit `Direkte Aenderung`, `direkt beheben`, `aendern`, `refactoren` oder aehnlich verlangt, arbeite im Modus `Direkte Aenderung`.
- Wenn der Nutzer keinen Modus nennt, nutze standardmaessig `Direkte Aenderung`, damit die bisherige Funktion erhalten bleibt.

Arbeite in dieser Reihenfolge:

1. Untersuche Controller, Services, Repositories, DTOs, Entities, Mapper und Exception-Handling.
2. Ordne jede Klasse ihrer tatsaechlichen Verantwortung zu.
3. Markiere Verstoesse gegen typische Spring-Boot-Konventionen.
4. Behebe saubere, risikoarme Verstoesse direkt, aber nur im Modus `Direkte Aenderung`.
5. Stoppe und erklaere Trade-offs, wenn eine groessere Architekturaenderung noetig waere.

Nutze fuer Reviews und Refactorings diese Regeln:

- Halte Controller schlank. Controller koordinieren HTTP, Request-Mapping, Response-Codes und DTO-Grenzen.
- Verschiebe Business-Logik in Services.
- Halte Repositories rein fuer Persistence-Zugriffe und Query-Methoden.
- Nutze Request-DTOs nur fuer Eingaben.
- Nutze Response-DTOs nur fuer Ausgaben.
- Gib Entities nicht direkt ueber API-Grenzen zurueck, sofern das Projekt bereits DTOs nutzt.
- Nutze Mapper nur fuer Umwandlungen, nicht fuer Business-Logik.
- Lege Validierung an Eingabegrenzen ab, typischerweise in Request-DTOs oder Controller-Methoden.
- Halte Exception-Handling konsistent und zentral, sofern bereits globale Fehlerbehandlung vorhanden ist.
- Halte Benennungen eindeutig: `*Controller`, `*Service`, `*Repository`, `*Request`, `*Response`, `*Mapper`, `*Exception`.
- Pruefe, ob Package-Namen und Klassennamen mit ihrer Rolle uebereinstimmen.

Pruefe insbesondere diese Anti-Patterns:

- Controller mit Datenbankzugriff oder Rechenlogik
- Repository mit Business-Logik
- Service, der HTTP-spezifische Details kennt, obwohl das vermeidbar ist
- DTOs, die gleichzeitig Input- und Output-Rolle mischen
- Entities mit API-spezifischem Verhalten
- Mapper mit Validierung, Berechnung oder Seiteneffekten
- inkonsistente oder irrefuehrende Klassennamen
- mehrere konkurrierende Wege fuer dieselbe Verantwortung

Im Modus `Bericht` analysierst du dieselben Punkte, setzt aber keine Aenderungen um.

Wenn du Aenderungen umsetzt:

- Bevorzuge kleine, lokale Refactorings.
- Bewahre das bestehende API-Verhalten, sofern der Nutzer nichts anderes verlangt.
- Ergaenze oder aktualisiere Tests, wenn vorhandene Tests betroffen sind oder ein Risiko entsteht.
- Halte Doku-Dateien wie Endpunktuebersichten synchron, wenn API-Struktur oder Namensgebung sichtbar betroffen ist.

Wenn du eine Review-Antwort schreibst:

- Liste Findings nach Schweregrad.
- Benenne konkrete Dateien und den Verstoss gegen die Schichten- oder Spring-Boot-Konvention.
- Sage explizit, in welchem Modus du gearbeitet hast.
- Sage explizit, ob du nur geprueft oder auch angepasst hast.

Wenn du unsicher bist, lies [references/checklist.md](references/checklist.md). Verwende die Checkliste als Standardmassstab, nicht als starres Dogma.
