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
- [X] [TELEGRAM] Add @ConfigurationProperties for telegram.-properties.
- [X] [API] Add an automatic request to ANRE API every `<value>` minutes. Must be optional depending on property (true/false)
- [X] [API] Implement stub for ANRE call, using property.
- [X] [CORE] Implement the WebClient configuration in a YAML file.
- [X] [CORE] Add SpotBugs maven plugin.
- [X] [TELEGRAM] Generalize fuel prices to avoid `if` and `switch` checks (at converter level).
- [X] [API] Add sorting by proximity to getAllFillingStations.
- [X] [TELEGRAM] Implement filling station cache.
- [X] [CORE] Migrate to Java 20 and Spring Boot 3.
- [X] [API] Add sort parameters in request.
- [X] [TELEGRAM] Add Flyway instead of `spring.jpa.hibernate.ddl-auto=update`.
- [X] [TELEGRAM] Implement exception rethrowing from third-party API.
- [X] [TELEGRAM] Implement request per time unit limiter.
- [X] [API] Add an endpoint to request the official fuel price from ANRE.
- [X] [API] Move `limit` and `offset` logic on the service layer.
- [X] [TELEGRAM] Remove user from a database in case he blocked telegram bot.

## Features

- [ ] [CORE] Add Dockerfile and Docker-compose to build a project in container.
- [ ] [API] Add Spring Security for REST API (using mock LDAP).
- [ ] [EXTERNAL] Add custom JWT token library.
- [ ] [CORE] Create a Kafka-Statistics module to collect data about the cities from which the request is made (implement through
  aspects and Thymeleaf).
- [ ] [CORE] Add SL4FJ logging in a project.
- [ ] [API] Add auditable for controllers (save data in database).
- [ ] [TELEGRAM] Three languages for displaying text in Telegram bot (using Internationalization in Spring Boot).
- [ ] [CORE] Add Actuator.
- [ ] [CORE] Add SonarQube.

## Food For Thought

- A component for creating advertisements for the Telegram bot
  (UI for adding date, video/pictures, text, etc. for an advertisement, and TARGET city).
- A component for sending advertisements in a chat with a bot, based on the user's city.
- The ad component and telegram bot can be libraries or microservices that use the core logic of Telegram Bot.
- Check the possibility of using the Telegram Bot Web Application API (as the second option for displaying the nearest filling
  stations)
- UI component for displaying statistics on REST and Telegram requests based on the Kafka-Statistics module.
- Java Swing applications that query the state of a component through the Spring Boot Actuator (Live Health Check Monitoring).
- Get rid of the limit of 10 filling stations in the radius and replace it with CallBackQuery with arrows (forward and backward).