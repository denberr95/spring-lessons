# Spring Boot 4 Project Guidance

## Stack

- Spring Boot 4.x, Spring Framework 7, Java 21
- Maven wrapper: `./mvnw`
- Jakarta EE 11, Jackson 3, and Boot 4 modular starters
- Use dedicated technology test starters rather than the classic test starter

## Engineering Rules

- Inspect neighboring code and the dependency tree before choosing an API or starter.
- Keep controllers as adapters and transactions in application services.
- Use DTOs; do not expose Hibernate entities as external contracts.
- Use `tools.jackson` APIs for Jackson customization.
- Use `SpringBootIntegrationTests` and use local infrastructure to emulate a really scenario.
- Add Flyway migrations for schema changes and never edit applied migrations.
- Preserve error, security, pagination, nullability, and observability contracts.
