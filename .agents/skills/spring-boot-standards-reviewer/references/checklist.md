# Checklist

Nutze diese Liste für schnelle, konsistente Bewertungen in Spring-Boot-Projekten.

## Architektur

- Liegt eine klassische Schichtentrennung vor: Controller -> Service -> Repository?
- Sind Verantwortlichkeiten klar getrennt?
- Ist die Package-Struktur verständlich und konsistent?

## Controller

- Enthält der Controller nur HTTP-nahe Logik?
- Nimmt er `*Request`-DTOs entgegen?
- Gibt er `*Response`-DTOs oder passende Response-Typen zurück?
- Vermeidet er Datenbankzugriffe und Geschäftslogik?

## Service

- Liegt Fachlogik im Service?
- Orchestriert der Service Repositories und Mapper sinnvoll?
- Enthält der Service keine unnötigen HTTP-Konzepte?

## Repository

- Kümmert sich das Repository nur um Persistence?
- Sind Query-Methoden passend benannt?
- Enthält das Repository keine Fachlogik?

## DTOs

- Werden Request-DTOs nur als Eingabemodelle genutzt?
- Werden Response-DTOs nur als Ausgabemodelle genutzt?
- Sind Felder und Namen fachlich klar?

## Entities

- Beschreibt die Entity das Persistenzmodell?
- Ist sie frei von API-spezifischer Darstellung?
- Enthält sie keine unpassende Controller- oder DTO-Logik?

## Mapper

- Ist Mapping von Fachlogik getrennt?
- Bleiben Mapper frei von Seiteneffekten?
- Nutzen sie saubere, vorhersagbare Konvertierungen?

## Validation und Fehlerbehandlung

- Sitzt Validation an der Eingabegrenze?
- Werden Fehler konsistent behandelt?
- Gibt es eine zentrale oder klar strukturierte Exception-Behandlung?

## Benennung

- Stimmen Klassenname und Verantwortlichkeit überein?
- Folgen Typen den ueblichen Suffixen wie `Request`, `Response`, `Service`, `Repository`, `Controller`?
- Gibt es irrefuehrende oder gemischte Bezeichnungen?
