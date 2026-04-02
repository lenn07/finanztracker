---
name: implement-feature
description: Implementiere kleine bis mittlere Features in einem bestehenden Projekt mit klarer Kontextanalyse, kleinem Change-Umfang und kurzer Ergebniszusammenfassung. Use when Codex ein Feature umsetzen, betroffene Komponenten identifizieren, einen Implementierungsplan ableiten, bei echten Unklarheiten Rueckfragen stellen, Aenderungen bewusst klein halten, README-Auswirkungen pruefen und ohne ausdrueckliche Anweisung keine Tests ergaenzen oder ausfuehren soll.
---

# Implement Feature

## Overview

Setze Features strukturiert und mit moeglichst wenig Seiteneffekten um. Verstehe zuerst die betroffenen Komponenten, leite dann einen konkreten Umsetzungsweg ab und halte die Aenderungen eng am eigentlichen Scope.

## Workflow

Arbeite in dieser Reihenfolge:

1. Finde die betroffenen Komponenten wie Controller, Services, Repositories, DTOs, Mapper, Templates, Migrationen oder Konfigurationsdateien.
2. Lies nur den relevanten Kontext und orientiere dich an bestehenden Mustern, bevor du aenderst.
3. Leite aus dem gefundenen Kontext einen kurzen Implementierungsplan ab.
4. Frage nach, wenn eine fachliche oder technische Unklarheit das Ergebnis wesentlich veraendern koennte.
5. Setze die Aenderung mit moeglichst kleinen, lokalen Anpassungen um.
6. Pruefe, ob README, Endpunkt-Doku oder andere sichtbare Projekt-Dokumentation mitgezogen werden muessen.
7. Liefere zum Schluss eine knappe Summary mit `was` und `warum`.

## Rueckfragen

Stoppe fuer eine Rueckfrage, wenn mindestens einer dieser Punkte zutrifft:

- Mehrere Loesungswege haben unterschiedliche fachliche Folgen.
- Das bestehende Verhalten ist widerspruechlich oder offensichtlich unvollstaendig.
- Die Aenderung wuerde API-Vertraege, Datenmodell, Security oder finanzbezogene Logik spuerbar beeinflussen.
- Fuer die Umsetzung waere eine groessere Struktur- oder Architekturentscheidung noetig.

Wenn die Richtung trotz Kontext nicht klar ist, zeige kurze Optionen mit Trade-offs statt Annahmen zu treffen.

## Guardrails

- Halte den Change-Umfang klein und bevorzuge bestehende Muster vor neuen Strukturen.
- Ergaenze oder aendere Tests nur, wenn der Nutzer das ausdruecklich verlangt.
- Fuehre Tests nicht ungefragt aus.
- Aendere keine Secrets, Zugangsdaten, Commits oder Pushes ohne ausdrueckliche Zustimmung.
- Weise explizit auf neue Dependencies, Migrationen oder groessere Strukturentscheidungen hin.

## Antwortformat

Antworte am Ende knapp und konkret:

- Nenne kurz die betroffenen Komponenten.
- Fasse die umgesetzten Aenderungen zusammen.
- Begruende in einem Satz pro groesserem Block, warum die Aenderung noetig war.
- Sage explizit, ob README angepasst wurde.
- Sage explizit, dass keine Tests ergaenzt oder ausgefuehrt wurden, falls das so ist.

## Beispiel-Trigger

- `Implementiere das Feature, dass Kategorie-Budgets im Monatswechsel vorbelegt werden.`
- `Use $implement-feature to add a filter reset button on the transactions page.`
- `Baue eine neue Detailansicht fuer Budgets ein und halte die Aenderungen klein.`
