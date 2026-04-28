# Claude Commands — Spring Lessons

This document lists the custom slash commands defined in `.claude/commands/` and available in every Claude Code session opened in this repository.

---

## Custom Slash Commands

Invoke a command by typing `/command-name` in the Claude Code prompt.

| Command | Description | Source |
| --- | --- | --- |
| `/commit-conventional` | Creates a git commit following Conventional Commits rules enforced by `.pre-commit-config.yaml` | [`.claude/commands/commit-conventional.md`](../.claude/commands/commit-conventional.md) |
| `/document-code-md` | Analyses the source code and keeps all Markdown documentation files up to date | [`.claude/commands/document-code-md.md`](../.claude/commands/document-code-md.md) |
| `/update-maven-deps` | Scans Maven dependencies and plugins for available stable version upgrades, without modifying any file | [`.claude/commands/update-maven-deps.md`](../.claude/commands/update-maven-deps.md) |

---

## Platform Skills

Skills installed globally via the Claude plugin system or available by default in Claude Code.

### Installed Plugins

| Plugin | Source |
| --- | --- |
| `skill-creator` | `claude-plugins-official` |

### Global Skills

Skills available in every Claude Code session. The following are particularly relevant to this project:

| Skill | When to use in this project |
| --- | --- |
| `init` | Initialize or regenerate `CLAUDE.md` with codebase documentation |
| `review` | Review an open pull request on GitHub |
| `security-review` | Perform a security audit of staged changes (OAuth2, Kafka, input validation) |
| `simplify` | Review recently changed code for quality, reuse and efficiency |
| `fewer-permission-prompts` | Scan session transcripts and add `allowlist` entries to `.claude/settings.json` |
| `update-config` | Configure hooks, permissions, and env vars in `settings.json` |
| `schedule` | Schedule a one-time or recurring remote agent (e.g. dependency check, cleanup PR) |
| `loop` | Run a command or prompt repeatedly on an interval |
| `skill-creator:skill-creator` | Create, edit, or evaluate custom skills for this project |

---

## Adding New Commands

1. Create a Markdown file in `.claude/commands/<command-name>.md`
2. Write the prompt that Claude will follow when the command is invoked
3. Restart Claude Code (or reload the session) to pick up the new command
4. Commit the file using `/commit-conventional` with scope `setup`

**Naming convention:** use lowercase kebab-case (e.g. `new-endpoint.md`, `check-deps.md`).

---

[← Back to README](../README.md)
