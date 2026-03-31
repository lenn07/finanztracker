# Checklist

- Liegt eine klare Schichtentrennung zwischen Controller, Service und Repository vor?
- Enthalten Controller nur HTTP-nahe Logik?
- Liegt Fachlogik im Service?
- Dienen Repositories nur dem Datenzugriff?
- Werden Request-DTOs nur fuer Eingaben verwendet?
- Werden Response-DTOs nur fuer Ausgaben verwendet?
- Bleiben Entities vom API-Vertrag getrennt?
- Bleiben Mapper frei von Business-Logik?
- Sitzt Validation an der Eingabegrenze?
- Ist Exception-Handling konsistent?
- Stimmen Klassenname, Package und Verantwortung ueberein?
- Sind moegliche Fixes lokal, sicher und ohne Contract-Bruch machbar?
