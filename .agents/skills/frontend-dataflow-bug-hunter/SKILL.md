---
name: frontend-dataflow-bug-hunter
description: Pruefe und verbessere Frontend-nahe Bugs in einem Spring-Boot-Webprojekt mit servergerenderten Views, Formularen und Datenuebergaben zum Backend. Use when Codex should review or debug Thymeleaf templates, page controllers, form DTOs, request DTOs, model attributes, validation flow, redirects, and frontend-backend data transfer issues.
---

# Frontend Dataflow Bug Hunter

Pruefe UI-Bugs, Formularfehler und Datenflussprobleme zwischen Frontend und Backend.

Arbeite immer in einem von zwei Modi:

- `Bericht`: Analysiere, finde Bugs und Risiken und schreibe nur einen Bericht. Nimm keine Dateiaenderungen vor.
- `Direkte Aenderung`: Analysiere wie bisher und behebe sichere, lokale Fehler direkt.

Bestimme den Modus so:

- Wenn der Nutzer explizit `Bericht`, `nur Bericht`, `nur pruefen`, `Review` oder aehnlich verlangt, arbeite im Modus `Bericht`.
- Wenn der Nutzer explizit `Direkte Aenderung`, `direkt beheben`, `aendern`, `fixen` oder aehnlich verlangt, arbeite im Modus `Direkte Aenderung`.
- Wenn der Nutzer keinen Modus nennt, nutze standardmaessig `Direkte Aenderung`, damit die bisherige Funktion erhalten bleibt.

Arbeite in dieser Reihenfolge:

1. Untersuche Templates, Page-Controller, Form-DTOs, Request-DTOs, Mapper und Service-Aufrufe.
2. Verfolge den Datenfluss von Eingabe im Frontend bis zur Verarbeitung im Backend und zur Rueckgabe ins Frontend.
3. Finde Inkonsistenzen bei Feldnamen, Binding, Validation, Redirects, Model-Attributen und Anzeigeformaten.
4. Behebe sichere, lokale Fehler direkt, aber nur im Modus `Direkte Aenderung`.
5. Stoppe bei groesseren Architekturentscheidungen und benenne die Ursache klar.

Pruefe insbesondere diese Fehlerklassen:

- `th:field`, `name`, `id` oder `th:object` passen nicht zu den DTO-Feldern
- Model-Attribute im Controller passen nicht zu den Erwartungen im Template
- Form-DTO und Request-DTO driften auseinander
- Validation-Fehler werden nicht korrekt zur View zurueckgegeben
- Redirects verlieren relevanten Kontext oder Filterwerte
- Datums-, Enum-, Zahlen- oder ID-Felder werden zwischen UI und Backend inkonsistent behandelt
- Select-Optionen, Default-Werte oder Listen fehlen im Fehlerfall erneut im Model
- API-Controller, Page-Controller und Templates bilden denselben Anwendungsfall unterschiedlich ab
- Erfolgs- und Fehlermeldungen werden nicht sichtbar oder nicht konsistent transportiert
- Frontend zeigt veraltete oder unvollstaendige Daten, obwohl das Backend korrekt liefert

Nutze fuer die Bewertung diese Regeln:

- Ein Template darf nur Model-Attribute verwenden, die der Controller garantiert setzt.
- Ein `*Form`-DTO dient der Formularbindung in servergerenderten Views.
- Ein `*Request`-DTO dient dem Backend-Eingang fuer API oder interne Service-Uebergabe.
- Feldnamen zwischen View, Form-DTO und Mapping muessen konsistent bleiben.
- Bei Validation-Fehlern muessen alle fuer die View noetigen Listen, Flags und Kontextwerte erneut gesetzt werden.
- Redirects sollen benoetigten UI-Kontext wie Filter oder Monat nicht unbeabsichtigt verlieren.
- Zahlen-, Datums- und Enum-Werte muessen in UI, Binding und Backend konsistent formatiert und geparst werden.

Im Modus `Direkte Aenderung` erlaube direkte Anpassungen ohne Rueckfrage, wenn sie lokal und sicher sind:

- fehlende oder falsche Model-Attribute nachziehen
- inkonsistente Feldnamen zwischen Template und DTO angleichen
- Validation-Fehlerpfade vervollstaendigen
- Redirect-Parameter oder Flash-Messages konsistent machen
- kleine Mapping- oder Binding-Fehler korrigieren
- offensichtliche Anzeige- oder Formularbugs im Template bereinigen

Im Modus `Bericht` analysierst du dieselben Punkte, setzt aber keine Aenderungen um.

Stoppe und erklaere das Risiko statt direkt umzubauen, wenn:

- API-Vertraege geaendert werden muessen
- DB-Modell oder groessere Fachlogik betroffen ist
- mehrere konkurrierende Datenfluesse aufgeloest werden muessen
- unklar ist, ob das aktuelle Verhalten absichtlich so modelliert wurde

Wenn du antwortest:

- Nenne zuerst konkrete Bugs oder Risiken.
- Referenziere die betroffenen Dateien.
- Sage explizit, in welchem Modus du gearbeitet hast.
- Sage explizit, welche Fehler nur analysiert und welche du direkt behoben hast.
- Trenne UI-Bugs, Binding-/Transfer-Bugs und groessere Strukturprobleme voneinander.

Wenn du unsicher bist, lies [references/checklist.md](references/checklist.md).
