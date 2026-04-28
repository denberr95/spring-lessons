# Claude Commands — Spring Lessons

This document describes the custom slash commands defined in `.claude/commands/` and available in every Claude Code session opened in this repository.

- [Custom Slash Commands](#custom-slash-commands)
- [Adding New Commands](#adding-new-commands)

---

## Custom Slash Commands

Invoke a command by typing `/command-name` in the Claude Code prompt.

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
   | `database` | Flyway migrations, JPA entities, schema changes, SQL files |
   | `docs` | Markdown files, documentation |
   | `tests` | Test sources under `src/test/` |
   | `setup` | `pom.xml`, `settings.xml`, `.pre-commit-config.yaml`, `.vscode/` |
   | `deploy` | `collections/compose-*.yaml`, `Containerfile`, `Dockerfile` |
   | `script` | Shell or batch scripts (e.g. `entrypoint.sh`) |
   | `report` | Observability config (Prometheus, Grafana, Loki) |
   | `ci` | CI/CD pipeline files |

4. Stages specific files (never `git add -A` or `git add .`)
5. Creates the commit with the correct message format — **description must always be in English**
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

### `/document-code-md`

**File:** `.claude/commands/document-code-md.md`

Analyzes the source code and keeps the Markdown documentation files up to date, reflecting the current state of the codebase.

**What it does:**

1. Discovers the project structure (build files, config, scripts, `.claude/commands/`)
2. Reads all `.md` files in the project root and `docs/` (excluding `.claude/`)
3. Compares documented content against actual code and identifies: obsolete info, missing sections, incorrect data, removed components
4. Manages four Claude-specific docs in `docs/`:
   - `CLAUDE_COMMANDS.md` ← `.claude/commands/`
   - `CLAUDE_SKILLS.md` ← installed plugins + relevant global skills
   - `CLAUDE_HOOKS.md` ← hooks in `settings.json`
   - `CLAUDE_AGENTS.md` ← subagent usage patterns
5. Applies only necessary edits — preserves structure, style, and navigation links
6. Produces a report of changes per file

**Usage:**

```text
/document-code-md
```

---

### `/update-maven-deps`

**File:** `.claude/commands/update-maven-deps.md`

Scans Maven dependencies and plugins for available stable version upgrades, without modifying any file.

**What it does:**

1. Reads `pom.xml` (parent BOM, properties, dependencies, plugins) and `settings.xml` if present
2. Runs `mvn versions:display-dependency-updates`, `display-plugin-updates`, `display-property-updates`
3. Filters out non-stable versions (alpha, beta, milestone, RC, snapshot)
4. Produces a structured report grouped by: Parent BOM, Properties, Dependencies, Plugins
5. Highlights high-impact upgrades (Spring parent, core libs) vs low-risk ones (build plugins)

**Usage:**

```text
/update-maven-deps
```

---

## Adding New Commands

1. Create a Markdown file in `.claude/commands/<command-name>.md`
2. Write the prompt that Claude will follow when the command is invoked
3. Restart Claude Code (or reload the session) to pick up the new command
4. Commit the file using `/commit-conventional` with scope `setup`

**Naming convention:** use lowercase kebab-case (e.g. `new-endpoint.md`, `check-deps.md`).

---

[← Back to README](../README.md)
