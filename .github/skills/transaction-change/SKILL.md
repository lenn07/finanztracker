---
name: transaction-change
description: Verwende diesen Skill, wenn Transaktionen, Buchungen, Einnahmen, Ausgaben oder zugehörige API-Endpunkte geändert werden.
---

## Ziel
Implementiere Änderungen an der Finanztracker-Transaktionslogik konsistent über Controller, Service, DTOs, Mapper, Repository und Tests.

## Regeln
- Keine Business-Logik im Controller.
- Für Geldbeträge immer BigDecimal verwenden.
- Niemals JPA-Entities direkt als API-Response zurückgeben.
- Request-Daten mit Jakarta Validation prüfen.
- Änderungen klein und lokal halten.

## Vorgehen
1. Finde alle betroffenen Controller, Services, DTOs, Mapper und Repositories.
2. Ergänze oder passe Request- und Response-DTOs an.
3. Halte Business-Regeln im Service.
4. Prüfe Query-Logik, Filter, Datumslogik und Summenberechnung.
5. Ergänze Unit-Tests für Business-Regeln.
6. Ergänze Integrationstests für API- oder Persistence-Verhalten.

## Prüffragen
- Ist die Response stabil und API-tauglich?
- Ist BigDecimal korrekt verwendet?
- Ist Validierung vorhanden?
- Sind Tests ergänzt?