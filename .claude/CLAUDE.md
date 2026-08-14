# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Full documentation lives in [`docs/`](../docs/): [Architecture](../docs/ARCHITECTURE.md) · [Setup](../docs/SETUP.md) · [Skills](../docs/CLAUDE_SKILLS.md) · [Agents](../docs/CLAUDE_AGENTS.md) · [Hooks](../docs/CLAUDE_HOOKS.md).

---

## Commands

Every `mvn` command requires `--settings settings.xml` (custom Maven settings at project root).

```bash
# Run application (macOS/Linux)
mvn spring-boot:run --file pom.xml --settings settings.xml -Dspring-boot.run.profiles=default,linux

# Build JAR (skip tests)
mvn package --file pom.xml --settings settings.xml -DskipTests=true

# Run all tests (infrastructure must be running — see below)
set -a && . collections/.env && set +a
mvn test --file pom.xml --settings settings.xml

# Run a single test class
mvn test --file pom.xml --settings settings.xml -Dtest=BooksServiceTest

# Apply Google Java code formatting (required before commit)
mvn spotless:apply --file pom.xml --settings settings.xml

# Start infrastructure (PostgreSQL, Kafka, Keycloak, observability stack)
podman compose --project spring-lessons --env-file ./collections/.env \
  --file ./collections/compose-env.yaml up --force-recreate --remove-orphans --detach
```

---

## Architecture

### Request flow

```text
Client (JWT Bearer)
  → Spring Security (OAuth2 Resource Server, stateless, no sessions)
  → @PreAuthorize scope check (e.g. hasAuthority('SCOPE_books:get'))
  → Controller interface (OpenAPI annotations only)
  → Controller impl (business delegation)
  → Service (@Observed, @Transactional)
  → Repository (JPA) → PostgreSQL
```

**Controller pattern:** every controller is split into an interface (`IBooksRestController`) carrying all `@Operation`/`@ApiResponse` annotations, and an implementation (`BooksRestController`) with `@PreAuthorize` and the actual delegation. Never put OpenAPI annotations on the implementation class.

**Exception handling:** two `@RestControllerAdvice` layers — `CommonRestControllerAdvice` (global, all domains) and `BooksRestControllerAdvice` (Books-only, scoped via `assignableTypes`). Each handler uses `@ExceptionHandler(X.class)` (no `value = {X.class}` array form — SonarQube S3878). Log with a literal string: `log.error("Book not found exception", exception)`, not `log.error(exception.getMessage(), exception)` (SonarQube S2629).

### Items — async Kafka flow

Items upload and delete are fire-and-forget via Kafka. The REST endpoint publishes a `KafkaMessageItemDTO` to `topic-items`; actual DB persistence happens in `ItemsKafkaListener`. Two consumer groups share the topic, routed by `RecordFilterStrategy`:

- `upload-items.group` → `UploadItemsRecordFilter` (ItemStatus == UPLOAD)
- `delete-items.group` → `DeleteItemsRecordFilter` (ItemStatus == DELETE)

**Critical:** `spring.json.trusted.packages` must include `com.personal.springlessons.model.dto.response` (not just `.model.dto`) because `KafkaMessageItemDTO` lives in the `response` sub-package. Spring Kafka does exact package matching, not prefix matching.

### Optimistic locking — Books

Two distinct failure modes with separate HTTP codes:

- **412 Precondition Failed** → `PreconditionFailedException`: the `If-Match` header value doesn't match the current `ETag`. Raised in `BooksService.verifyIfMatch()` before the DB write. Wildcard `If-Match: *` bypasses this check.
- **409 Conflict** → `ConcurrentUpdateException`: `If-Match` matched, but another transaction committed between the read and the write (JPA `OptimisticLockException`). Raised at flush time.

ETag format: `W/"<version>"`. Stored as `@Version long` on `BooksEntity`.

### Audit trail (Hibernate Envers)

`BooksEntity` is `@Audited`. `CustomRevisionEntity` extends Envers `DefaultRevisionEntity` with five extra fields captured by `CustomRevisionEntityListener` from the current `SecurityContext` and `HttpServletRequest`: `ipAddress`, `clientId`, `username`, `requestUri`, `httpMethod`. Schema: `history` (tables: `revinfo`, `books_audit`).

### Security

`SecurityConfig` is stateless OAuth2 Resource Server. Keycloak issuer: `http://localhost:8080/realms/master`. Six Keycloak client applications map to six roles, each granting a subset of `books:*` / `items:*` scopes. Full role × scope matrix in [`docs/ARCHITECTURE.md`](../docs/ARCHITECTURE.md#security).

---

## Key conventions

**Code style:** Google Java Style enforced by Spotless. Run `mvn spotless:apply` before committing; the pre-commit hook will reject non-formatted code.

**Commits:** Conventional Commits format enforced by pre-commit. Use `/commit-conventional` skill to generate the commit message.

**Spring profiles:**

- Development: `default,linux` (macOS/Linux) or `default,windows`
- Container runtime: `runtime` (all config from env vars)
- AOT / native-image build: `aot` (H2 in-memory, Flyway off, Kafka off — avoids external connections during `process-aot`)

**JaCoCo exclusions:** `SpringLessonsApplication`, `component/mapper/**`, `config/**`, `model/**`.

**HTTP status codes:** use `HttpStatus.CONTENT_TOO_LARGE` (not deprecated `PAYLOAD_TOO_LARGE`) for 413 responses — Spring Framework 7 / RFC 9110.

---

## Testing gotchas

Integration tests (`@SpringBootTest`) connect to real PostgreSQL, Kafka, and Keycloak. Before running from CLI, load the env vars:

```bash
set -a && . collections/.env && set +a
```

VS Code loads them automatically via `envFile` in `.vscode/settings.json` → `java.test.config`.

The Kafka topic (`topic-items`) accumulates messages between test runs. If a poison message is stuck at offset 0 and blocks a partition, recreate the broker:

```bash
podman compose --project spring-lessons --env-file ./collections/.env \
  --file ./collections/compose-env.yaml up --force-recreate --detach kafka
```

---

## Infrastructure image versions

All container image tags are pinned in `collections/.env` as `IMAGE_*` variables. To update them, use the `/update-docker-images` skill.

---

## Custom skills

Project-specific skills in `.claude/skills/`:

| Skill | Purpose |
| --- | --- |
| `/commit-conventional` | Git commit following Conventional Commits |
| `/document-code-md` | Keep `docs/*.md` in sync with the source code |
| `/update-docker-images` | Bump pinned container image versions in `.env` |
| `/update-maven-deps` | Scan Maven dependencies and plugins for available upgrades |
