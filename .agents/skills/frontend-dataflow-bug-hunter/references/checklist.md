# Checklist

## Template und View

- Passt `th:object` zum gesetzten Model-Attribut?
- Stimmen `th:field`, Feldname und DTO-Property ueberein?
- Werden alle im Template genutzten Model-Attribute im Controller gesetzt?
- Bleiben Anzeigeformate fuer Datum, Betrag und Enum konsistent?

## Page-Controller

- Werden Form-DTOs korrekt initialisiert?
- Werden bei Validation-Fehlern alle noetigen Listen, Flags und Kontextwerte erneut ins Model gelegt?
- Bleiben Filter, Monat oder andere UI-Kontexte bei Redirects erhalten?
- Stimmen View-Name und erwartete Template-Datei ueberein?

## Datenuebergabe

- Passt `Form -> Request -> Service` fachlich und technisch zusammen?
- Gehen Felder auf dem Weg verloren oder werden falsch gemappt?
- Sind Default-Werte konsistent?
- Werden IDs, Enums, Zahlen und Datumswerte korrekt gebunden?

## Fehlerbilder

- Gibt es leere Select-Listen nach Fehlern?
- Gibt es Validation-Fehler, die im Frontend nicht erscheinen?
- Gibt es Erfolgsmeldungen, die nach Redirects verloren gehen?
- Gibt es Seiten, die Daten anzeigen, die nicht zum aktuellen Filter oder Datensatz passen?
