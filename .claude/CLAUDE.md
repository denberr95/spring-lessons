# Spring Boot 4 Project Guidance

This file provides guidance to Claude Code (claude.ai/code) when working with code with Spring Boot 4.

---

## Stack

- **Framework:** Spring Boot 4.x
- **Language:** Java 25
- **Web Server:** Jetty (replaces Tomcat)
- **Security:** Spring Security — OAuth2 Resource Server + JWT
- **Identity Provider:** Keycloak
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA + Hibernate
- **Audit:** Hibernate Envers
- **DB Migrations:** Flyway
- **Messaging:** Apache Kafka
- **DTO Mapping:** MapStruct 1.6.3
- **API Documentation:** OpenAPI 3 (SpringDoc — Swagger UI + Scalar)
- **Observability:** Micrometer + OpenTelemetry + Prometheus + Grafana + Jaeger
- **Logging:** Logback (JSON) + Loki
- **Email:** JavaMail + Mailpit
- **HTTP Client:** Apache HttpClient 5
- **SOAP:** Spring-WS + JAXB (XSD-generated)
- **Build:** Maven 3.9+
- **Code Quality:** Spotless (Google Java Style)
- **SBOM:** CycloneDX Maven Plugin
- **Containerization:** Podman + Containerfile

---

## Commands

### Application

- **Maven Clean:** `mvn clean --file pom.xml --settings.xml`
- **Run (macOS/Linux):** `mvn spring-boot:run --file pom.xml --settings settings.xml -Dspring-boot.run.profiles=default,linux`
- **Run (Windows):** `mvn spring-boot:run --file pom.xml --settings settings.xml -Dspring-boot.run.profiles=default,windows`
- **Build JAR (skip tests):** `mvn package --file pom.xml --settings settings.xml -DskipTests=true`
- **Build JAR:** `mvn package --file pom.xml --settings settings.xml` (Infrastructure must be running before executing integration tests)

### Testing

- **Load env vars (required before running tests):** `set -a && . collections/.env && set +a`
- **Run all tests:** `mvn test --file pom.xml --settings settings.xml`
- **Run a single test class:** `mvn test --file pom.xml --settings settings.xml -Dtest=<TestClassName>`
- **Generate test coverage report:** `mvn verify --file pom.xml --settings settings.xml`

## Precommit

- **Install hooks:** `pre-commit install`
- **Autoupdate:** `pre-commit autoupdate`
- **Run All Hooks:** `pre-commit run --all-files`

### Code Quality

- **Apply Google Java code formatting:** `mvn spotless:apply --file pom.xml --settings settings.xml`

### Local Infrastructure

- **Start:** `podman compose --project spring-lessons --env-file ./collections/.env --file ./collections/compose-env.yaml up --force-recreate --remove-orphans --detach`
- **Stop:** `podman compose --project spring-lessons --file ./collections/compose-env.yaml down`
- **Cleanup Containers**: `podman container rm --all --force`
- **Cleanup Images**: `podman image rm --all --force`
- **Cleanup Volumes**: `podman volume rm --all --force`
- **System Prune**: `podman system prune --force --all --volumes`

### Specific Container Service

- **Export Keycloack Local Configuration:** `podman compose --project spring-lessons --env-file ./collections/.env --file ./collections compose-keycloak-export.yaml up --abort-on-container-exit --remove-orphans`

---

## Skills

Use matching skill files under `.claude/skills/`. Each skill encodes a repeatable workflow; invoke them by name when the task matches.

- **`/commit-conventional`**: Generates a Conventional Commits-compliant commit message based on staged changes.
- **`/spring-boot-4-null-safety`**: Sets up JSpecify-based null safety — adds `@NullMarked` to all packages via `package-info.java`, adds the JSpecify dependency to `pom.xml`, and migrates any legacy null annotations to `org.jspecify.annotations`.
- **`/update-maven-deps`**: Updates Maven dependencies to their latest compatible versions and verifies the build is not broken.
- **`/update-docker-images`**: Updates container image tags in Podman Compose files to their latest available versions and verifies the local infrastructure starts correctly.
- **`/update-precommit-hooks`**: Updates all pre-commit hook revisions to their latest versions, verifies Python compatibility for Python-based hooks, and reverts incompatible updates to the highest compatible revision.

---

## Conventions

- **Code style:** Google Java Style enforced by Spotless. Run the [Apply Google Java code formatting](#code-quality) command before committing; the pre-commit hook will reject non-formatted code.
- **Commits:** Conventional Commits format enforced by pre-commit. Use `/commit-conventional` skill to generate the commit message.
- **Spring Profiles:** Use OS-specific profiles for local development and testing. Always combine with `default`. See [Run commands](#application).
  - **MacOS/Linux:** `-Dspring-boot.run.profiles=default,linux`
  - **Windows:** `-Dspring-boot.run.profiles=default,windows`
  - **Container/CI:** `-Dspring-boot.run.profiles=runtime` (all config injected via env vars)
- **Test coverage:** JaCoCo measures test coverage at build time. See [Generate test coverage report](#testing). The build fails if coverage thresholds are not met — do not disable or bypass them. Report available at `target/site/jacoco/index.html`.
- **Local infrastructure:** Podman Compose manages all required services (PostgreSQL, Kafka, Keycloak, observability stack). Infrastructure must be running before executing integration tests. See [Local Infrastructure commands](#local-infrastructure).
- **Project layout — special folders:** The following directories have a specific role and must be used consistently:
  - **`bin/`**: Stores compiled binaries (JAR or native image). Never commit its contents.
  - **`logs/`**: Stores application logs generated during local testing. Never commit its contents.
  - **`docs/`**: Contains project documentation. Must be updated whenever a new feature is developed.
  - **`collections/`**: Contains Podman Compose files, `.env` files, and Postman collections. Required for local infrastructure and API testing.
