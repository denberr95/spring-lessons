# Spring Boot Lessons

- [Software](#software)
- [Environment](#environment)
- [VSCode Configuration files](#vscode-configuration-files)
- [API Security](#api-security)
  - [Roles](#roles)
  - [Scopes](#scopes)
  - [Matrix Permissions](#matrix-permissions)
  - [Client Applications](#client-applications)

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

| **Name** | **Description** | **Link** |
| :-: | :-: | :-: |
| Environment Compose | Compose file to generate environment | [compose-env.yaml](./container/compose.yaml) |
| Application Containerfile | Containerfile to build Spring Application in jvm version | [Containerfile.jvm](Containerfile.jvm) |
| Application Compose | Compose file to run Spring Application in environment | [compose-app.yaml](./deploy/compose.yaml) |
| Postman Collection | Collection for testing API | [postman.json](./collections/postman.json) |
| Jaeger UI | Trace & Spans Monitoring | <http://localhost:16686/search> |
| Prometheus | System & Application Metrics | <http://localhost:9090> |
| Grafana | Dashboard & Alert Monitoring | <http://localhost:3000/grafana> |
| Database Spring | PostgreSQL Database Spring Application | jdbc:postgresql://localhost:5432/spring |
| Kafka Broker | Kafka Bootstrap Server | localhost:9092 |
| Keycloak | Keycloak Administration Console | <http://localhost:8080> |
| Spring Lessons | Spring Application Web Server | <http://localhost:10000> |
| Spring Lessons | Spring Application Actuator | <http://localhost:10001> |

## VSCode Configuration files

| **Name** | **Description** | **Link** |
| :-: | :-: | :-: |
| Application Runner | Configuration file for run application | [Launcher](.vscode/launch.json) |
| Settings | Configuration VS Code | [VS Code Settings](.vscode/settings.json) |
| Automation Tasks | Configuration script task | [Tasks](.vscode/tasks.json) |

## API Security

### Roles

- #### Role Books API

    | **Role** | **Read** | **Write** | **Delete** |
    | :-: | :-: | :-: | :-: |
    | api-books-admin | X | X | X |
    | api-books-writer | X | X | |
    | api-books-reader | X | | |

- #### Role Items API

    | **Role** | **Read** | **Write** | **Delete** |
    | :-: | :-: | :-: | :-: |
    | api-items-admin | X | X | X |
    | api-items-writer | X | X | |
    | api-items-reader | X | | |

### Scopes

- #### Scope Books API

    | **Scope** | **Read** | **Write** | **Delete** |
    | :-: | :-: | :-: | :-: |
    | books:get | X | | |
    | books:save | | X | |
    | books:delete | | | X |
    | books:update | | X | |

- #### Scope Items API

    | **Scope** | **Read** | **Write** | **Delete** |
    | :-: | :-: | :-: | :-: |
    | items:get | X | | |
    | items:upload | | X | |
    | items:delete | | | X |

### Matrix Permissions

- #### Books API Mapping Scopes-Role

    | | **api-books-admin** | **api-books-reader** | **api-books-writer** |
    | :- | :-: | :-: | :-: |
    | books:get | X | X | X |
    | books:save | X | | X |
    | books:delete | X | | |
    | books:update | X | | X |

- #### Items API Mapping Scopes-Role

    | | **api-items-admin** | **api-items-reader** | **api-items-writer** |
    | :- | :-: | :-: | :-: |
    | items:get | X | X | X |
    | items:upload | X | | X |
    | items:delete | X | | |

### Client Applications

- #### Scopes Books Client Applications

    | | **client-id-books-admin** | **client-id-books-reader** | **client-id-books-writer** |
    | :- | :-: | :-: | :-: |
    | books:get | X | X | X |
    | books:save | X | | X |
    | books:delete | X | | |
    | books:update | X | | X |

- #### Scopes Items Client Applications

    | | **client-id-items-admin** | **client-id-items-reader** | **client-id-items-writer** |
    | :- | :-: | :-: | :-: |
    | items:get | X | X | X |
    | items:upload | X | | X |
    | items:delete | X | | |
