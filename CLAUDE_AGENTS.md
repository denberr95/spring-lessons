# Claude Agents & Skills — Spring Lessons

This document describes the Claude Code agents and custom slash commands available in this repository.

- [Custom Slash Commands](#custom-slash-commands)
- [Recommended Agents](#recommended-agents)
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
5. Creates the commit with the correct message format and `Co-Authored-By` trailer
6. If a pre-commit hook fails, diagnoses the error, fixes it, and creates a **new** commit (never `--amend`)

**Usage:**

```
/commit-conventional
```

**Commit message format:**

```
<type>(<scope>): <description>
```

Example: `feat(api): add paginated endpoint for order items`

---

## Recommended Agents

The following agents are not yet implemented as custom commands but are suggested for this project. They can be invoked directly as natural-language prompts to Claude Code.

### Explore agent

Use for broad codebase exploration when a simple search is not enough:

- "Explore the Kafka consumer flow end to end"
- "Find all endpoints that are missing `@Observed`"
- "List all Spring beans that are not covered by tests"

### Plan agent

Use before starting non-trivial implementation tasks:

- "Plan a new REST endpoint for X following the existing pattern"
- "Plan the Flyway migration for adding column Y to table Z"

### Suggested future commands

| Command | Description |
| --- | --- |
| `/new-endpoint` | Scaffold a new REST endpoint (controller interface + impl, service, DTO, mapper, `@PreAuthorize`, `@Observed`) |
| `/new-migration` | Generate the next numbered Flyway script following naming conventions |
| `/check-deps` | Run `mvn versions:display-dependency-updates` and summarise available upgrades |
| `/otel-check` | Verify all endpoints have `@Observed` and Kafka listeners have Micrometer spans |

---

## Adding New Commands

1. Create a Markdown file in `.claude/commands/<command-name>.md`
2. Write the prompt that Claude will follow when the command is invoked
3. Restart Claude Code (or reload the session) to pick up the new command
4. Commit the file using `/commit-conventional` with scope `setup`

**Naming convention:** use lowercase kebab-case (e.g. `new-endpoint.md`, `check-deps.md`).
