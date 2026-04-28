# Claude Skills — Spring Lessons

This document describes the Claude Code skills available in this project — both project-level commands exposed as skills and globally installed platform skills relevant to this workflow.

- [Project Skills](#project-skills)
- [Platform Skills](#platform-skills)
- [Installing New Skills](#installing-new-skills)

---

## Project Skills

The following slash commands defined in `.claude/commands/` are also exposed as invocable skills:

| Skill | Trigger | Description |
| --- | --- | --- |
| `commit-conventional` | `/commit-conventional` | Automates Conventional Commits with scope enforcement and English description |
| `document-code-md` | `/document-code-md` | Syncs Markdown documentation with current codebase state |
| `update-maven-deps` | `/update-maven-deps` | Reports available stable Maven dependency/plugin upgrades |

See [CLAUDE_COMMANDS.md](CLAUDE_COMMANDS.md) for full documentation of each command.

---

## Platform Skills

Skills installed globally via the Claude plugin system or available by default in Claude Code.

### Installed Plugins

| Plugin | Source | Version |
| --- | --- | --- |
| `skill-creator` | `claude-plugins-official` | unknown |

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

## Installing New Skills

Skills can be installed from the Claude plugin marketplace or created locally:

```bash
# Install a plugin skill
claude plugin install <plugin-name>

# Create a new project-level skill
# → add a .md file to .claude/commands/ and commit it with scope `setup`
```

See [CLAUDE_COMMANDS.md — Adding New Commands](CLAUDE_COMMANDS.md#adding-new-commands) for the project command convention.

---

[← Back to README](../README.md)
