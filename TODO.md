# TODO

## Done

- [X] Release an initial bot version.
- [X] Create unit and slice tests for basic functionality.
- [X] Create REST API.
- [X] Create unit and slice tests for REST API.
- [X] Configure PostgreSQL for project.
- [X] Add Maven Failsafe and Surefire plugins and separate tests goals (it and test).
- [X] Add Jacoco Maven plugin.
- [X] Configure Webhook setup on application startup.
- [X] Add Dependency Check maven plugin.
- [X] Add automatic generation of a ngrok https tunnel. Should be an optional function depending on property (true/false).
- [X] Add checkstyle plugin.
- [X] Replace REST description in README.md with swagger auto documentation.
- [X] Separate Telegram and Rest modules (pseudo-microservice architecture).
- [X] [CORE] Implement Jacoco report-aggregate goal.

## Important Features

- [ ] [CORE] Add Dockerfile and Docker-compose to build a project in container.
- [ ] [API] Add Spring Security for REST API (using mock LDAP).
- [ ] [EXTERNAL] Add custom JWT token library.
- [ ] [CORE] Create a Kafka-Statistics module to collect data about the cities from which the request is made (implement through
  aspects and Thymeleaf).
- [ ] [TELEGRAM] Add Flyway instead of `spring.jpa.hibernate.ddl-auto=update`.
- [ ] [CORE] Add SL4FJ logging in a project.
- [ ] [API] Add auditable for controllers (save data in database).
- [ ] [API] Implement stub for ANRE call, using property (for local testing).
- [ ] [TELEGRAM] Implement exception rethrowing from third-party API.

## Secondary

- [ ] [TELEGRAM] Remove user from a database in case he blocked telegram bot.
- [ ] [API] Fetch ANRE API on fast startup in a separate thread.
- [ ] [API] Create WebFlux tests with WebTestClient.
- [ ] [TELEGRAM] Three languages for displaying text in Telegram bot (using Internationalization in Spring Boot).
- [ ] [API] Add an automatic request to ANRE API every 15 minutes. Must be optional depending on property (true/false)
- [ ] [TELEGRAM] Add @ConfigurationProperties for telegram.-properties.
- [ ] [CORE] Add Actuator.
- [ ] [API] Analyze mapstruct warning "Unmapped target properties."
- [ ] [TELEGRAM] Implement filling station cache.
- [ ] [API] Read `filling-station.api-` from YAML file.
- [ ] [CORE] Add findbugs maven plugin.
- [ ] [TELEGRAM] Implement request per time unit limiter.