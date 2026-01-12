# Fridge Cleaner

Fridge Cleaner is a JavaFX desktop app that helps track fridge inventory and manage recipes so food gets used before it expires. The UI is in French and focuses on quick entry plus clean, card-based screens.

## What it does
- Add items to an inventory with quantity, unit, and optional expiry date
- View inventory in a table and adjust quantities with quick actions
- Create recipes with servings, instructions, and ingredient lists
- Store everything in a local SQLite database (`fridge.db`)

## Tech stack
- Java 17
- JavaFX 21 (FXML + controllers)
- SQLite (via `sqlite-jdbc`)
- Maven

## Architecture
- UI layer: FXML views + JavaFX controllers
- Service layer: simple orchestration around repositories
- Repository layer: JDBC access to SQLite
- Domain model: `Item`, `Product`, `Recipe`, `Ingredient`, `Unit`

Database schema is created on startup by `SchemaInitializer`.

## Run locally
Prerequisites: JDK 17 and Maven.

```bash
mvn javafx:run
```

## Project structure
- `src/main/java/com/MaudeLebeau/fridgecleaner/app` entry point
- `src/main/java/com/MaudeLebeau/fridgecleaner/ui` JavaFX controllers
- `src/main/java/com/MaudeLebeau/fridgecleaner/service` services
- `src/main/java/com/MaudeLebeau/fridgecleaner/repository` JDBC repositories
- `src/main/java/com/MaudeLebeau/fridgecleaner/domain` domain model
- `src/main/resources/com/MaudeLebeau/fridgecleaner/ui` FXML + CSS

## Notes
- The database file is stored locally as `fridge.db`. Delete it to reset data.
- Some navigation items (Prices, Settings) are placeholders.

## Why this project
Built to showcase desktop UI development, clean layering, and persistent data modeling with a real SQLite schema.
