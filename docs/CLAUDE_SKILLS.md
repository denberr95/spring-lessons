# Claude Skills — Spring Lessons

This document lists the custom skills defined in `.claude/skills/` and available in every Claude Code session opened in this repository.

---

## Custom Skills

Invoke a skill by typing `/skill-name` in the Claude Code prompt.

| Skill | Description | Source |
| --- | --- | --- |
| `/commit-conventional` | Creates a git commit following Conventional Commits rules enforced by `.pre-commit-config.yaml` | [`.claude/skills/commit-conventional/SKILL.md`](../.claude/skills/commit-conventional/SKILL.md) |
| `/document-code-md` | Analyses the source code and keeps all Markdown documentation files up to date | [`.claude/skills/document-code-md/SKILL.md`](../.claude/skills/document-code-md/SKILL.md) |
| `/update-maven-deps` | Scans Maven dependencies and plugins for available stable version upgrades, without modifying any file | [`.claude/skills/update-maven-deps/SKILL.md`](../.claude/skills/update-maven-deps/SKILL.md) |

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

## Adding New Skills

1. Create a directory in `.claude/skills/<skill-name>/`
2. Create a `SKILL.md` file inside it with YAML frontmatter and the skill body:
   ```yaml
   ---
   name: skill-name
   description: When to trigger this skill (used by Claude to decide auto-invocation)
   disable-model-invocation: true
   ---
   ```
3. Write the prompt that Claude will follow when the skill is invoked
4. Restart Claude Code (or reload the session) to pick up the new skill
5. Commit using `/commit-conventional` with scope `setup`

**Naming convention:** use lowercase kebab-case (e.g. `new-endpoint`, `check-deps`).

---

[← Back to README](../README.md)
