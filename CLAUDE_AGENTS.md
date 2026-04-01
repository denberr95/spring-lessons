# Claude Agents & Skills — Spring Lessons

This document describes the Claude Code agents and custom slash commands available in this repository.

- [Custom Slash Commands](#custom-slash-commands)
- [Adding New Commands](#adding-new-commands)

---

## Custom Slash Commands

Custom slash commands live in `.claude/commands/` and are available in every Claude Code session opened in this repository. Invoke them by typing `/command-name` in the prompt.

### `/commit-conventional`

**File:** `.claude/commands/commit-conventional.md`

Automates git commits following the Conventional Commits rules enforced by `.pre-commit-config.yaml`.

**What it does:**

1. Runs `git status` and `git diff HEAD` to analyse staged and unstaged changes
2. Selects the appropriate commit **type** (`feat`, `fix`, `docs`, `refactor`, `chore`, `ci`, `build`, `perf`, `test`, `style`, `revert`)
3. Forces one of the valid **scopes** accepted by the `conventional-pre-commit` hook:

   | Scope | When to use |
   | --- | --- |
   | `api` | Controllers, DTOs, REST/SOAP endpoints, OpenAPI spec |
   | `code` | Service, component, config, model, repository, util, exception |
   | `database` | Flyway migrations, JPA entities, schema changes |
   | `docs` | Markdown files, documentation |
   | `tests` | Test sources under `src/test/` |
   | `setup` | `pom.xml`, `settings.xml`, `.pre-commit-config.yaml`, `.vscode/`, `Containerfile` |
   | `deploy` | `collections/compose-*.yaml`, `Containerfile` |
   | `script` | Shell scripts, `entrypoint.sh` |
   | `report` | Observability config (Prometheus, Grafana, Loki) |
   | `ci` | CI/CD pipeline files |

4. Stages specific files (never `git add -A` or `git add .`)
5. Creates the commit with the correct message format
6. If a pre-commit hook fails, diagnoses the error, fixes it, and creates a **new** commit (never `--amend`)

**Usage:**

```text
/commit-conventional
```

**Commit message format:**

```text
<type>(<scope>): <description>
```

Example: `feat(api): add paginated endpoint for order items`

---

## Adding New Commands

1. Create a Markdown file in `.claude/commands/<command-name>.md`
2. Write the prompt that Claude will follow when the command is invoked
3. Restart Claude Code (or reload the session) to pick up the new command
4. Commit the file using `/commit-conventional` with scope `setup`

**Naming convention:** use lowercase kebab-case (e.g. `new-endpoint.md`, `check-deps.md`).
