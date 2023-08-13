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

## Important Features

- [ ] Add Dockerfile and Docker-compose to build a project in container.
- [ ] Add Spring Security for REST API (using mock LDAP).
- [ ] Add custom JWT token library.
- [ ] Create a Kafka-Statistics module to collect data about the cities from which the request is made (implement through
  aspects and Thymeleaf).
- [ ] Separate Telegram and Rest modules (pseudo-microservice architecture).
- [ ] Add Flyway instead of `spring.jpa.hibernate.ddl-auto=update`.
- [ ] Add SL4FJ logging in a project.

## Secondary

- [ ] Remove user from a database in case he blocked telegram bot.
- [ ] Fetch ANRE API on fast startup in a separate thread.
- [ ] Create WebFlux tests with WebTestClient.
- [ ] Three languages for displaying text in Telegram bot (using Internationalization in Spring Boot).
- [ ] Replace REST description in README.md with swagger auto documentation.
- [ ] Add an automatic request to ANRE API every 15 minutes. Must be optional depending on property (true/false)
- [ ] Add @ConfigurationProperties for telegram.-properties.
- [ ] Add Actuator.
- [ ] Analyze mapstruct warning "Unmapped target properties."