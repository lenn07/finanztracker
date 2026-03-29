# Projektkontext
Dieses Repository ist eine Spring-Boot-REST-API für einen Finanztracker.

## Architektur
- Nutze die Schichten Controller -> Service -> Repository.
- Verwende Lombok für Getter, Setter, Konstruktoren und Builder, um Boilerplate-Code zu vermeiden.
- Keine Business-Logik in Controllern.
- Controller geben nur DTOs zurück, niemals JPA-Entities.
- Mapping zwischen Entity und DTO erfolgt zentral in Mappern.
- Validierung erfolgt mit Jakarta Validation an Request-DTOs.
- Fehler werden zentral über @ControllerAdvice behandelt.

## Datenmodell
- Änderungen an JPA-Entities müssen auf Fetch-Type, Cascade, N+1-Risiken und JSON-Serialisierung geprüft werden.
- Verwende BigDecimal für Geldbeträge, niemals double oder float.
- Währungen und Beträge müssen präzise und nachvollziehbar behandelt werden.
- Datenbankänderungen nur mit passender Migration.

## API-Regeln
- Endpunkte REST-konform benennen.
- Bei API-Änderungen immer OpenAPI/Swagger, Tests und Beispiel-Requests prüfen.
- Breaking Changes klar benennen.

## Qualität
- Schreibe bei neuer Business-Logik Unit-Tests.
- Bei Änderungen an Persistence oder API-Verhalten Integrationstests ergänzen.
- Änderungen müssen mit Gradle/Maven-Testlauf validiert werden.
- Bevorzuge kleine, gezielte Refactorings statt großer Umbauten.
- Halte den Code einheitlich und sauber (Clean Code Prinzipien beachten).

## Sicherheit
- Keine Secrets hardcoden.
- Auth/Security-Konfiguration nicht vereinfachen oder entfernen.
- Eingaben immer validieren.