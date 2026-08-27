---
name: update-precommit-hooks
description: Update pre-commit hook revisions to their latest available versions, verify Python compatibility for Python-based hooks against the system Python, revert any incompatible updates to the highest compatible revision, and smoke-test the result. Use this skill whenever the user asks about pre-commit hook updates, hook maintenance, or keeping the pre-commit configuration current.
---

# Update pre-commit hooks

Update all hook revisions in `.pre-commit-config.yaml` to their latest available versions, enforcing compatibility with the system Python for Python-based hooks.

## Procedure

### 1. Detect the environment

Run:

```bash
python3 --version
pre-commit --version
```

Record the system Python version (e.g. `3.9.6`). This version is the hard ceiling for any Python-based hook.

### 2. Run autoupdate

```bash
pre-commit autoupdate 2>&1
```

`pre-commit autoupdate` modifies `.pre-commit-config.yaml` in place. Note every repo whose `rev` changed (e.g. `v1.37.1 -> v1.38.0`).

### 3. Classify each updated hook by runtime

For every updated repo, determine whether its hook runs in a Python environment:

| Runtime | How to identify | Compatibility check needed? |
|---|---|---|
| Python | repo on PyPI (yamllint, sqlfluff, …) | **Yes** |
| Node.js | `language: node` or npm-based CLI (markdownlint-cli) | No |
| System / Podman | `language: system`, entry uses `podman run` | No |
| Go binary | `language: golang` | No |

A repo is Python-based when its hook installs a PyPI package. Common examples in this project: `yamllint`, `sqlfluff`, `conventional-pre-commit`, `pre-commit-hooks`.

### 4. Verify Python compatibility for Python-based hooks

For each updated Python-based hook, map the GitHub repo to its PyPI package name and run:

```bash
pip3 install --dry-run <package>==<new-version> 2>&1 | grep -i "requires\|python\|error"
```

**Compatible** → dry-run exits successfully, keep the updated rev.

**Incompatible** (output contains `Requires-Python` or `requires a different python`) → the new version requires a higher Python. Find the highest installable version:

```bash
pip3 index versions <package> 2>&1 | head -3
```

The first version listed is the highest compatible with the system Python. Set `rev` in `.pre-commit-config.yaml` to that version.

### 5. Apply corrections

Edit `.pre-commit-config.yaml` to pin incompatible hooks to their highest compatible rev. Keep all non-Python hook updates as-is.

### 6. Smoke-test

Stage the config file and run the fastest Python-based hook (typically `yamllint`) to confirm theupdated environment installs correctly:

```bash
git add .pre-commit-config.yaml
pre-commit run --hook-stage pre-commit yamllint 2>&1| tail -10
```

If the hook passes, the environment is healthy. If it fails with a Python version error, repeat
step 4 for that hook.

### 7. Report

Present a summary table:

```text
| Hook                   | Previous | Updated to | Status                         |
|------------------------|----------|------------|--------------------------------|
| markdownlint-cli       | v0.49.0  | v0.49.1    | ✅ updated (Node.js)           |
| yamllint               | v1.37.1  | v1.37.1    | ⚠ reverted (requires Py≥3.10)  |
| hadolint               | v2.14.0  | v2.15.1    | ✅ updated (Podman)            |
| sqlfluff                | 4.2.2    | 4.1.0      | ⚠ fixed — 4.2+ requires Py≥3.10 |
```

For any hook pinned below the true latest, add a note explaining which Python version would unlock the update (e.g. "upgrade to Python ≥3.10 to use sqlfluff 4.3.0").
