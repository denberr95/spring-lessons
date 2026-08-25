# Setup & Requirements — Spring Lessons

- [Local Environment](#local-environment)
- [VS Code Tasks Reference](#vs-code-tasks-reference)

---

## Local Environment

### Application endpoints

| Service | URL |
| --- | --- |
| Keycloak | <http://localhost:8080> |
| Jaeger UI | <http://localhost:16686/search> |
| Prometheus | <http://localhost:9090> |
| Grafana | <http://localhost:3000> |
| Mailpit UI | <http://localhost:8025> |
| Wiremock admin | <http://localhost:9998/__admin/> |
| Application base | <http://localhost:8888/spring-app> |
| Swagger UI | <http://localhost:8888/spring-app/swagger-ui/index.html> |
| Scalar UI | <http://localhost:8888/spring-app/scalar> |
| Actuator | <http://localhost:8889/actuator> |
| Database | <jdbc:postgresql://localhost:5432/spring> |
| Kafka broker | <localhost:29092> |

## VS Code Tasks Reference

Tasks are defined in `.vscode/tasks.json`.

### Lifecycle tasks

| Task | Description |
| --- | --- |
| `start-podman-env` | Clean up, start containers, compile sources |
| `stop-podman-env` | Stop containers, remove volumes, export Keycloak realm |
| `test-application` | Full test cycle (clean env → start → package + test) |
| `cleanup-env` | Full Podman cleanup (containers, images, volumes, system prune) |

### Build tasks

| Task | Description/Command |
| --- | --- |
| `compile-source-code` | `mvn clean compile` |
| `mvn-clean` | `mvn clean` |
| `mvn-compile` | `mvn compile` |
| `mvn-package` | `mvn package` (with tests) |
| `mvn-package-skip-tests` | `mvn package -DskipTests=true` |
| `mvn-test` | `mvn test` |
| `mvn-spotless-apply` | Apply Google Java code formatting |
| `podman-build-image` | Build the container image |

### Container tasks

| Task | Description |
| --- | --- |
| `podman-compose-env-up` | Start environment services |
| `podman-compose-env-stop` | Stop environment services |
| `podman-compose-app-up` | Start environment + app container |
| `podman-compose-app-stop` | Stop environment + app container |

### Utility tasks

| Task | Description |
| --- | --- |
| `mvn-dependencies-tree` | Output dependency tree to `logs/maven-dependency-tree.log` |
| `mvn-display-dependency-updates` | Output available dependency updates to `logs/` |
| `mvn-display-plugin-updates` | Output available plugin updates to `logs/` |
| `pre-commit-autoupdate` | Update pre-commit hook versions |
| `pre-commit-run-all-files` | Run all pre-commit checks on every file |
| `podman-compose-keycloak-export` | Export Keycloak realm configuration |

---

### API Versioning

All application API endpoints (`/books`, `/items`) require the `API-Version` header:

```http
API-Version: 1
```

Requests that omit this header receive `400 Bad Request`. Actuator endpoints (`/actuator/**`) and OpenAPI endpoints (`/v3/api-docs`, `/swagger-ui/**`, `/scalar`) are unversioned and do not require the header.

### Postman collection

Import `./collections/postman.json` into Postman for a ready-made request collection covering all API endpoints. The `API-Version: 1` header is pre-configured on every Books and Items request.

---

[← Back to README](../README.md)
