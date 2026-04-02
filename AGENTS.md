# AGENTS.md

## Projekt
**Finanztracker** ist eine Web-App zur Verwaltung persönlicher Finanzen mit Budgetplanung und Finanzübersicht.

## Stack
- Java
- Spring Boot
- Thymeleaf
- Maven
- PostgreSQL
- Flyway

## Grundregeln
- Kommuniziere auf **Deutsch** und **erklärend**
- Verstehe zuerst den relevanten Kontext und orientiere dich an der bestehenden Struktur
- Arbeite proaktiv und mache Verbesserungsvorschläge
- **Triff bei Unklarheiten keine Annahmen**; zeige Optionen auf und frage nach
- Begründe am Ende kurz, **was** geändert wurde und **warum**

## Erlaubt
- Code schreiben, refactoren, Bugs beheben, dokumentieren, recherchieren
- neue Dateien anlegen
- Architektur und Struktur anpassen, wenn sinnvoll
- Flyway-Migrationen erstellen oder ändern
- neue Dependencies hinzufügen, wenn nötig

## Nicht ohne ausdrückliche Zustimmung
- Secrets oder Zugangsdaten ändern
- Commits erstellen
- Änderungen pushen
- Tests erstellen/ausführen

## Konventionen
- Nutze die bestehende Spring-Boot-Standardstruktur
- Halte allgemeine Konventionen möglichst ein, insbesondere bei Spring Boot und REST
- Bevorzuge bestehende Muster vor unnötig neuen Strukturen
- Gehe besonders sorgfältig mit Konfigurationsdateien, Dockerfiles und finanzbezogener Logik um

## Qualität
- Änderungen sollen nachvollziehbar, konsistent und auf den eigentlichen Scope begrenzt sein
- Wenn Tests nicht ausgeführt werden, prüfe die Änderung zumindest auf offensichtliche Fehler und Konsistenz
- Aktualisiere bei relevanten Änderungen die `README.md`
- Weise ausdrücklich auf neue Dependencies, Migrationen oder größere Strukturänderungen hin