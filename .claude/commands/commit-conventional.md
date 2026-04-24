# Commit Conventional

Crea un git commit seguendo le regole di Conventional Commits definite in `.pre-commit-config.yaml`.

## Regole obbligatorie

Il commit-msg hook usa `conventional-pre-commit` con `--strict` e `--force-scope`.
Il messaggio DEVE rispettare il formato esatto:

```text
<type>(<scope>): <description>
```

**Scopes validi** (unici accettati dall'hook): `api`, `setup`, `docs`, `tests`, `deploy`, `script`, `report`, `database`, `code`

**Tipi validi**: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `ci`, `build`, `revert`

## Procedura

1. Esegui `git status` per vedere i file modificati, staged e untracked.
2. Esegui `git diff HEAD` per analizzare le modifiche nel dettaglio.
3. In base alle modifiche, scegli:
   - Il **tipo** più appropriato (feat per nuove funzionalità, fix per bug, docs per documentazione, refactor per refactoring, chore per manutenzione, ecc.)
   - Lo **scope** corretto tra quelli validi:
     - `api` → modifiche a controller, DTO, endpoint REST/SOAP, OpenAPI
     - `code` → modifiche a service, component, config, model, repository, util, exception
     - `database` → modifiche a migration Flyway (`src/main/resources/db/`), entità JPA, schema
     - `docs` → modifiche a file `.md`, documentazione
     - `tests` → modifiche a test (`src/test/`)
     - `setup` → modifiche a `pom.xml`, `settings.xml`, `.pre-commit-config.yaml`, `Containerfile`, `.vscode/`, `Dockerfile`
     - `deploy` → modifiche a `collections/compose-*.yaml`, `Containerfile`, `Dockerfile`
     - `script` → modifiche a script shell, `entrypoint.sh`
     - `report` → modifiche a configurazioni di osservabilità (Prometheus, Grafana, Loki)
     - `ci` → modifiche a pipeline CI/CD

4. Redigi una descrizione concisa in inglese (imperativo, minuscolo, senza punto finale, max 72 caratteri incluso il prefisso).

5. Se ci sono file untracked da includere, aggiungili con `git add <file>` prima del commit. Non usare mai `git add -A` o `git add .`.

6. Crea il commit con:

   ```bash
   git commit -m "<type>(<scope>): <description>"
   ```

7. Se il pre-commit hook fallisce:
   - Leggi l'errore per capire quale hook ha fallito.
   - Correggi il problema (es. `spotless:apply` per la formattazione Java, fix del file YAML/JSON, ecc.).
   - Re-esegui lo stage dei file corretti e crea un **nuovo** commit (mai `--amend`).
   - Ripeti finché il commit non viene accettato.

8. Al termine mostra il risultato di `git log -1 --oneline`.
