---
name: commit-conventional
description: Create a git commit following Conventional Commits format with project-defined scopes (api, setup, docs, tests, deploy, script, report, database, code). Use when the user asks to commit changes, save changes to git, finishes a feature or fix and wants to persist it, or requests a properly formatted commit message.
disable-model-invocation: true
---

# Commit Conventional

Create a git commit following the Conventional Commits rules defined in `.pre-commit-config.yaml`.

## Mandatory rules

The `commit-msg` hook uses `conventional-pre-commit` with `--strict` and `--force-scope`.
The message MUST follow this exact format:

```text
<type>(<scope>): <description>
```

**Valid scopes** (only these are accepted by the hook): `api`, `setup`, `docs`, `tests`, `deploy`, `script`, `report`, `database`, `code`

**Valid types**: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `ci`, `build`, `revert`

**Language**: `<description>` MUST always be in **English**, regardless of the language used by the user.

## Procedure

1. Run `git status` to inspect modified, staged, and untracked files.
2. Run `git diff HEAD` to analyze the changes in detail.
3. Based on the changes, select:
   - The most appropriate **type** (`feat` for new features, `fix` for bugs, `docs` for documentation, `refactor` for refactoring, `chore` for maintenance, etc.)
   - The correct **scope** from the valid list:
     - `api` → controllers, DTOs, REST/SOAP endpoints, OpenAPI specs
     - `code` → services, components, configuration, models, repositories, utils, exceptions
     - `database` → Flyway migrations (`src/main/resources/db/`), JPA entities, schema, SQL files
     - `docs` → `.md` files, project documentation
     - `tests` → test files under `src/test/`
     - `setup` → `pom.xml`, `settings.xml`, `.pre-commit-config.yaml`, `.vscode/`
     - `deploy` → `collections/compose-*.yaml`, `Containerfile`, `Dockerfile`
     - `script` → shell or batch scripts (e.g. `entrypoint.sh`)
     - `report` → observability configuration (Prometheus, Grafana, Loki)
     - `ci` → CI/CD pipeline files

4. Write a concise description in English (imperative mood, lowercase, no trailing period, max 72 characters including the prefix).

5. If there are untracked files to include, stage them explicitly with `git add <file>` before committing. Never use `git add -A` or `git add .`.

6. Create the commit:

```bash
   git commit -m "<type>(<scope>): <description>"
```

7. If the pre-commit hook fails:
   - Read the error output to identify which hook failed.
   - Fix the issue (e.g. run `spotless:apply` for Java formatting, fix malformed YAML/JSON, etc.).
   - Re-stage the corrected files and create a **new** commit — never use `--amend`.
   - Repeat until the commit is accepted.

8. On success, display the result of `git log -1 --oneline`.