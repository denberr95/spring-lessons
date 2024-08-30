# Spring Boot Lessons

- [Software](#software)
- [Environment](#environment)
- [VSCode Launcher](#vscode-launcher)
- [API Security](#api-security)
  - [Roles](#roles)
  - [Scopes](#scopes)
  - [Matrix Permissions](#matrix-permissions)

## Software

| **Name** | **Description** | **Link** |
| :-: | :-: | :-: |
| Visual Studio Code | IDE | <https://code.visualstudio.com> |
| Java 17 | Program Language | <https://adoptium.net/en-GB/temurin/releases/> |
| Git | Source Control Manager | <https://git-scm.com> |
| Maven | Java Development Tool | <https://maven.apache.org> |
| Podman | Container Runtime | <https://podman.io> |
| Podman Compose | Container Runtime Tool | <https://github.com/containers/podman-compose> |
| Postman | API Testing Tool | <https://www.postman.com> |
| Flyway | Database Development Tool | <https://flywaydb.org/> |
| Apache Kafka | Message Broker | <https://kafka.apache.org/> |
| Keycloak | Identity and Access Management | <https://www.keycloak.org/> |
| OpenTelemetry | Observability framework and toolkit | <https://opentelemetry.io/> |

## Environment

- [Environment Compose](./container/compose.yaml)
- [Application Containerfile](Containerfile.jvm)
- [Application Compose](./deploy/compose.yaml)
- [Postman Collection](./collections/postman.json)

| **Name** | **Description** | **Link** |
| :-: | :-: | :-: |
| Jaeger UI | Trace & Spans Monitoring | <http://localhost:16686/search> |
| Prometheus | System Metrics | <http://localhost:9090> |
| Grafana | Dashboard & Alert Monitoring | <http://localhost:3000/grafana> |
| Database Spring | PostgreSQL Database Spring Application | jdbc:postgresql://localhost:5432/spring |
| Kafka Broker | Kafka Bootstrap Server | localhost:9092 |
| Keycloak | Keycloak Administration Console | <http://localhost:8080> |
| Spring Lessons | Spring Application Web Server | <http://localhost:10000> |
| Spring Lessons | Spring Application Actuator | <http://localhost:10001> |

## VSCode Launcher

| | **TODO** | **TODO** |
| :-: | :-: | :-: |
| TODO | X | X |

## API Security

### Roles

#### Role Books API

| **Role** | **Read** | **Write** | **Delete** |
| :-: | :-: | :-: | :-: |
| api-books-admin | X | X | X |
| api-books-writer | X | X | |
| api-books-reader | X | | |

#### Role Items API

| **Role** | **Read** | **Write** | **Delete** |
| :-: | :-: | :-: | :-: |
| api-items-admin | X | X | X |
| api-items-writer | X | X | |
| api-items-reader | X | | |

### Scopes

#### Scope Books API

| **Scope** | **Read** | **Write** | **Delete** |
| :-: | :-: | :-: | :-: |
| books:get | X | | |
| books:save | | X | |
| books:delete | | | X |
| books:update | | X | |

#### Scope Items API

| **Scope** | **Read** | **Write** | **Delete** |
| :-: | :-: | :-: | :-: |
| items:get | X | | |
| items:upload | | X | |
| items:delete | | | X |

### Matrix Permissions

#### Books API Mapping

| | **api-books-admin** | **api-books-reader** | **api-books-writer** |
| :- | :-: | :-: | :-: |
| books:get | X | X | X |
| books:save | X | | X |
| books:delete | X | | |
| books:update | X | | X |

#### Items API Mapping

| | **api-items-admin** | **api-items-reader** | **api-items-writer** |
| :- | :-: | :-: | :-: |
| items:get | X | X | X |
| items:upload | X | | X |
| items:delete | X | | |
