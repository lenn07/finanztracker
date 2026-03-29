---
name: persistence-safety-review
description: Verwende diesen Skill bei Änderungen an JPA-Entities, Repositories, Relationen, Queries oder Datenbank-Migrationen.
---

## Ziel
Verhindere typische Fehler in Persistence- und Datenmodelländerungen.

## Regeln
- Prüfe FetchType, Cascade und orphanRemoval bewusst.
- Gib Entities niemals direkt über die REST-API zurück.
- Vermeide N+1-Probleme.
- Prüfe Serialisierung bei Relationen.
- Schemaänderungen nur mit passender Migration.

## Vorgehen
1. Prüfe betroffene Entities und Relationen.
2. Prüfe Repository-Methoden und Custom Queries.
3. Achte auf Lazy-/Eager-Auswirkungen.
4. Prüfe JSON-Serialisierung und Mapper.
5. Ergänze Migrationsdateien bei Schemaänderungen.
6. Ergänze Repository- oder Integrationstests.

## Prüffragen
- Entsteht ein N+1-Risiko?
- Ist die Relation API-sicher?
- Ist eine Migration nötig?
- Sind bestehende Queries noch korrekt?