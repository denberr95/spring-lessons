# Architecture

- [Project Structure](#project-structure)
- [Security](#security)
- [Database](#database)
- [Audit Trail](#audit-trail)
- [Observability](#observability)

---

## Project Structure

```text
src/main/java/com/personal/springlessons/
├── controller/
│   ├── books/              # BooksRestController + IBooksRestController
│   ├── items/              # ItemsRestController + IItemsRestController
│   └── CommonRestControllerAdvice.java
├── service/
│   ├── books/              # BooksService
│   ├── items/              # ItemsService
│   └── email/              # EmailService
├── component/
│   ├── kafka/              # ItemsKafkaListener
│   │   └── filter/         # UploadItemsRecordFilter, DeleteItemsRecordFilter
│   ├── access/             # CustomAuthenticationEntryPoint, CustomAccessDeniedHandler
│   ├── mapper/             # MapStruct mappers (BookMapper, ItemsMapper)
│   ├── httpclient/         # AccountsClient (REST client)
│   ├── event/              # Spring Application Events
│   ├── interceptor/        # HTTP interceptors
│   └── filter/             # Servlet filters
├── config/
│   ├── SecurityConfig.java
│   ├── KafkaTopicsConfig.java
│   ├── AppPropertiesConfig.java
│   ├── RestClientConfig.java
│   └── ...
├── model/
│   ├── entity/
│   │   ├── books/          # BooksEntity
│   │   ├── items/          # ItemsEntity, OrderItemsEntity
│   │   └── revision/       # CustomRevisionEntity (Envers)
│   ├── dto/                # BookDTO, OrderItemsDTO, ItemDTO, ...
│   ├── lov/                # Channel, Genre, ItemStatus (enums)
│   └── csv/                # CSV models for import/export
├── repository/
│   ├── books/              # IBooksRepository
│   └── items/              # IItemsRepository, IOrderItemsRepository
├── endpoint/               # SOAP endpoint (PlatformHistoryEndpoint)
├── exception/              # Custom exception hierarchy
└── util/                   # Utility classes
```

---

## Security

**Strategy:** Stateless OAuth2 Resource Server. No sessions, no CSRF.

```text
Client ──── Bearer JWT ────► Spring Security Filter Chain
                                        │
                                        ▼
                             JWT validation (JWK endpoint)
                                        │
                              ┌─────────▼──────────┐
                              │   Keycloak realm   │
                              └────────────────────┘
                                        │
                                  JWT claims extracted
                                        │
                              @PreAuthorize scope check
                              e.g. hasAuthority('SCOPE_books:get')
```

### Roles × Scopes Matrix

| Scope | Type | `api-books-admin` | `api-books-writer` | `api-books-reader` | `api-items-admin` | `api-items-writer` | `api-items-reader` |
| --- | :-: | :-: | :-: | :-: | :-: | :-: | :-: |
| `books:get` | READ | X | X | X | | | |
| `books:download` | READ | X | X | X | | | |
| `books:save` | WRITE | X | X | | | | |
| `books:update` | WRITE | X | X | | | | |
| `books:upload` | WRITE | X | X | | | | |
| `books:delete` | DELETE | X | | | | | |
| `items:get` | READ | | | | X | X | X |
| `items:upload` | WRITE | | | | X | X | |
| `items:delete` | DELETE | | | | X | | |

### Client Applications × Roles Matrix

| Client Application | `api-books-admin` | `api-books-writer` | `api-books-reader` | `api-items-admin` | `api-items-writer` | `api-items-reader` |
| --- | :-: | :-: | :-: | :-: | :-: | :-: |
| `client-id-books-admin` | X | | | | | |
| `client-id-books-writer` | | X | | | | |
| `client-id-books-reader` | | | X | | | |
| `client-id-items-admin` | | | | X | | |
| `client-id-items-writer` | | | | | X | |
| `client-id-items-reader` | | | | | | X |

---

## Database

**Engine:** PostgreSQL

### Schema Layout

| Schema | Purpose |
| --- | --- |
| `spring_app` | Application tables (books, items, order_items) |
| `history` | Envers audit tables (books_audit, revinfo) |
| `flyway` | Flyway migration metadata |

### Flyway Migrations

Managed via `spring.flyway.*`. Scripts live in `src/main/resources/db/`.

---

## Audit Trail

Implemented with **Hibernate Envers** (`@Audited`).

Currently audited entities: `BooksEntity`.

**How it works:**

1. Every INSERT / UPDATE / DELETE on `spring_app.books` creates a revision record in `history.revinfo`.
2. The corresponding snapshot is stored in `history.books_audit`.
3. `CustomRevisionEntity` enriches each revision with request metadata (IP, OAuth2 client, username, URI, HTTP method) captured by `CustomRevisionEntityListener`.

```text
HTTP Request
     │
     ├── (IP, clientId, username, requestUri, method)
     │            captured by CustomRevisionEntityListener
     ▼
history.revinfo       ──────  history.books_audit
(rev, revtstmp,              (rev, revtype, book fields...)
 ipAddress, clientId,
 username, requestUri,
 httpMethod)
```

Setting `store_data_at_delete = true` ensures book data is preserved even after deletion.

---

## Observability

Full three-pillar observability (metrics, traces, logs) via OpenTelemetry.

### Architecture

```text
Spring App
│
├── Micrometer Observations (@Observed, Spans)
│         │
│         ▼
│   OpenTelemetry SDK
│         │
│         ▼
│   OTLP Collector
│         │
│         ├── Traces  ──► Jaeger
│         ├── Metrics ──► Prometheus
│         │                    └──► Grafana
│         └── Logs    ──► Loki
│                          └──► Grafana
│
└── /actuator/prometheus
          └──► Prometheus scrape
```

---

[← Back to README](../README.md)
