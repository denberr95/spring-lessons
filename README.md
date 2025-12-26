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

| **Name** | **Link** |
| :-: | :-: |
| Visual Studio Code | <https://code.visualstudio.com> |
| Java 21 | <https://bell-sw.com/pages/downloads/> |
| Git | <https://git-scm.com> |
| Maven | <https://maven.apache.org> |
| Podman | <https://podman.io> |
| Podman Compose | <https://github.com/containers/podman-compose> |
| Postman | <https://www.postman.com> |
| Flyway | <https://flywaydb.org/> |
| Apache Kafka | <https://kafka.apache.org/> |
| Keycloak | <https://www.keycloak.org/> |
| OpenTelemetry | <https://opentelemetry.io/> |
| Prometheus | <https://prometheus.io/> |
| Grafana | <https://grafana.com/> |
| Wiremock | <https://wiremock.org/> |
| Mailpit | <https://mailpit.axllent.org/> |
| Pre Commit | <https://pre-commit.com/> |

## Environment

| **Name** | **Link** |
| :-: | :-: |
| Environment Compose | [compose.yaml](./collections/compose.yaml) |
| Application Containerfile | [Containerfile.jvm](Containerfile.jvm) |
| Postman Collection | [postman.json](./collections/postman.json) |
| Jaeger UI | <http://localhost:16686/search> |
| Prometheus | <http://localhost:9090> |
| Grafana | <http://localhost:3000/grafana> |
| Database Spring | jdbc:postgresql://localhost:5432/spring |
| Kafka Broker | localhost:29092 |
| Keycloak | <http://localhost:8080> |
| Spring Lessons APIs | <http://localhost:10000> |
| Spring Lessons Actuator | <http://localhost:10001> |
| Swagger UI | <http://localhost:10000/spring-app/swagger-ui/index.html> |
| Wiremock | <http://localhost:9998/__admin/> |
| Mailpit | <http://localhost:8025/> |
| SMTP Server | localhost:1025 |

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
    | books:download | X | | |
    | books:upload | | X | |

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
    | books:download | X | X | X |
    | books:upload | X | | X |

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
    | books:download | X | X | X |
    | books:upload | X | | X |

- #### Scopes Items Client Applications

    | | **client-id-items-admin** | **client-id-items-reader** | **client-id-items-writer** |
    | :- | :-: | :-: | :-: |
    | items:get | X | X | X |
    | items:upload | X | | X |
    | items:delete | X | | |
