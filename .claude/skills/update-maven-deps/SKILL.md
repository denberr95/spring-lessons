---
name: update-maven-deps
description: Scan Maven dependencies and plugins for available stable updates and produce a categorized report (Parent BOM, properties, dependencies, plugins). Excludes pre-release versions (alpha, beta, RC, SNAPSHOT) and vendor-specific builds. Use this skill whenever the user asks about dependency updates, wants to check for outdated Maven libraries, mentions pom.xml health, is preparing a maintenance release, or needs a dependency audit — even if they don't explicitly say "Maven" or "update report". Trigger also when the user asks things like "are my libs up to date?", "what needs upgrading?", or "check my Java dependencies".
disable-model-invocation: true
---

# Update Maven Dependencies

Analyze the project's Maven dependencies and plugins and produce a report of available updates to stable versions.

## Procedure

### 1. Read the project

- Read `pom.xml` to identify:
  - Parent BOM (e.g. `spring-boot-starter-parent`) and its version
  - `<properties>` section with explicitly declared versions
  - `<dependencyManagement>` section with imported BOMs
  - `<dependencies>` section with all direct dependencies
  - `<build><plugins>` section with all plugins and their versions
- If `settings.xml` exists in the project root, use it in Maven invocations with `--settings settings.xml`
- If it does not exist, omit the flag

### 2. Run the scan

Execute the following Maven commands in sequence. If a command fails, report it and continue with the next.

**Dependency updates:**

```bash
mvn versions:display-dependency-updates --file pom.xml [--settings settings.xml] -DprocessDependencyManagement=true -DprocessDependencies=true 2>&1
```

**Plugin updates:**

```bash
mvn versions:display-plugin-updates --file pom.xml [--settings settings.xml] 2>&1
```

**Property updates:**

```bash
mvn versions:display-property-updates --file pom.xml [--settings settings.xml] 2>&1
```

### 3. Analyze the output

Filter results keeping **only updates to stable versions**. Exclude:

- Alpha, beta, milestone, release candidate versions (`-alpha`, `-beta`, `-M*`, `-RC*`, `-SNAPSHOT`, `-EA`)
- Versions identical to the current one
- Dependencies managed by the parent BOM without an explicit version in `pom.xml` (unless a BOM update itself is available)

### 4. Report

Present the results organized by category:

```text
## Maven Dependency Update Report

### Parent BOM
| Artifact | Current | Latest Stable | Notes |
|---|---|---|---|
| spring-boot-starter-parent | x.y.z | x.y.z | ... |

### Properties (explicit versions)
| Property | Artifact | Current | Latest Stable |
|---|---|---|---|
| ... | ... | ... | ... |

### Dependencies
| GroupId:ArtifactId | Current | Latest Stable |
|---|---|---|
| ... | ... | ... |

### Plugins
| GroupId:ArtifactId | Current | Latest Stable |
|---|---|---|
| ... | ... | ... |

### No updates available
- List of dependencies/plugins already at the latest stable version
```

If no updates are available for a category, indicate it with `— no updates available`.

### 5. Guidance

After the report, provide guidance on:

- **High-impact** updates (parent BOM, core Spring dependencies) that may require compatibility checks
- **Safe** updates (standalone libraries, build plugins) that can be applied with low risk
- Any **deprecated or obsolete dependencies** worth considering for replacement

Do not apply any changes to `pom.xml`. The report is informational only.
After the report, if there are updates to apply, suggest the user modify `pom.xml` and use `/commit-conventional` with scope `setup`.
