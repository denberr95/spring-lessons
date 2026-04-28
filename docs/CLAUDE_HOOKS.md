# Claude Hooks — Spring Lessons

This document describes the Claude Code hooks configured for this project.

Hooks are shell commands executed automatically by the Claude Code harness in response to specific events (tool calls, session start/stop, etc.). They are defined in `.claude/settings.json` (project scope) or `~/.claude/settings.json` (global scope).

- [Project Hooks](#project-hooks)
- [Global Hooks](#global-hooks)
- [Configuring Hooks](#configuring-hooks)

---

## Project Hooks

**Source:** `.claude/settings.json`

> No project-level hooks are currently configured.

---

## Global Hooks

**Source:** `~/.claude/settings.json`

> No global hooks are currently configured.

---

## Configuring Hooks

Hooks are defined under the `hooks` key in `settings.json`. Each hook specifies:

- `event` — the trigger event (e.g. `PostToolUse`, `PreToolUse`, `Stop`, `Notification`)
- `matcher` — optional filter (e.g. tool name, file pattern)
- `hooks[].type` — `"command"` for shell commands
- `hooks[].command` — the shell command to execute

Example structure:

```json
{
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "command",
            "command": "echo 'Bash tool used'"
          }
        ]
      }
    ]
  }
}
```

Use `/update-config` to add or modify hooks without editing the JSON manually.

---

[← Back to README](../README.md)
