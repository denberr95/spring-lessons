# Claude Agents — Spring Lessons

This document describes how Claude Code subagents are used in this project — which agent types to spawn, when, and for what purpose.

- [Available Agent Types](#available-agent-types)
- [Usage Patterns in This Project](#usage-patterns-in-this-project)

---

## Available Agent Types

| Agent type | Model | Strengths | Tools available |
| --- | --- | --- | --- |
| `Explore` | fast | File search, grep, codebase Q&A | All except Agent, Edit, Write |
| `Plan` | — | Architecture design, implementation plans | All except Agent, Edit, Write |
| `general-purpose` | — | Multi-step research, complex tasks, web search | All tools |
| `claude-code-guide` | — | Claude Code CLI, API, SDK questions | Bash, Read, WebFetch, WebSearch |

---

## Usage Patterns in This Project

### Codebase exploration

Use `Explore` when searching for symbols, patterns or answering structural questions that span multiple files. Specify thoroughness: `quick`, `medium`, or `very thorough`.

```text
Agent(Explore): "find all classes annotated with @KafkaListener and describe their consumer group config"
Agent(Explore, very thorough): "how does optimistic locking flow from the REST layer to the DB?"
```

### Architecture planning

Use `Plan` before implementing a non-trivial feature (new endpoint, Kafka consumer, Flyway migration) to align on approach and trade-offs before writing code.

```text
Agent(Plan): "design the implementation plan for adding a new SOAP endpoint that queries books_audit"
```

### Documentation and research

Use `general-purpose` for tasks that require reading files AND writing/editing, or for web research combined with code changes.

```text
Agent(general-purpose): "research the latest SpringDoc 3.x release notes and update pom.xml"
```

### Claude Code / API questions

Use `claude-code-guide` for questions about Claude Code features, hooks syntax, MCP servers, or Anthropic API usage.

```text
Agent(claude-code-guide): "how do I configure a PostToolUse hook that runs spotless after every Edit?"
```

---

[← Back to README](../README.md)
