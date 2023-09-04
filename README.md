# Application Ecosystem of Moldavian Filling Stations

<b>This application ecosystem is made for educational purposes only and is not used for commercial purposes.</b>

<p>This pet project was created to summarize the knowledge I have gained over 2+ years of Java development. I used all my skills 
in building application architecture, customizing third-party tools and libraries and much more.</p>

- [API](#api)
- [Structure](#structure)
- [Tools](#tools)
- [TODO](#todo)
- [Useful Resources](#useful-resources)

# API

To identify the best fuel price, and filling station location was used: [ANRE-API](https://api.iharta.md/anre/public/)

# Structure

- [Contract-Generator](/contract-generator) - is a library used in the bundle between Rest-API and Telegram-Bot; Java classes are
  generated using Swagger OpenAPI generator.
- [Report-Aggregate](/report-aggregate) - is a module that allows to aggregate Jacoco test coverage report data.
- [REST-API](/rest-api) - is a web service offering a set of REST APIs for working with filling stations in Moldova.
- [Telegram-Bot](/telegram-bot) - is a bot that uses Telegram messenger for comfortable search of information about locations and
  prices of Moldovan filling stations.

# Tools

1. [JDK 20.0.2](https://jdk.java.net/20/)
2. [Telegram Bots Spring Boot Starter](https://mvnrepository.com/artifact/org.telegram/telegrambots-spring-boot-starter)
3. [Spring Boot Starter](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter)
4. [Spring Boot Starter Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation)
5. [Spring Boot Starter Cache](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache)
6. [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
7. [Spring Boot Starter WebFlux](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webflux)
8. [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
9. [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test)
10. [SpringDoc OpenAPI Starter WebMVC UI](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui)
11. [H2](https://mvnrepository.com/artifact/com.h2database/h2)
12. [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
13. [Proj4J](https://mvnrepository.com/artifact/org.locationtech.proj4j/proj4j)
14. [Proj4J-EPSG](https://mvnrepository.com/artifact/org.locationtech.proj4j/proj4j-epsg)
15. [Mapstruct](https://mvnrepository.com/artifact/org.mapstruct/mapstruct)
16. [Ehcache](https://mvnrepository.com/artifact/org.ehcache/ehcache)
17. [Mockito Core](https://mvnrepository.com/artifact/org.mockito/mockito-core)
18. [Mockito Inline](https://mvnrepository.com/artifact/org.mockito/mockito-inline)
19. [JUnit](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api)
20. [WireMock](https://mvnrepository.com/artifact/com.github.tomakehurst/wiremock)
21. [PostgreSQL](https://mvnrepository.com/artifact/org.postgresql/postgresql)
22. [Maven Surefire Plugin](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-surefire-plugin)
23. [Maven Failsafe Plugin](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-failsafe-plugin)
24. [Maven Jacoco Plugin](https://mvnrepository.com/artifact/org.jacoco/jacoco-maven-plugin)
25. [Maven Dependency Check Plugin](https://mvnrepository.com/artifact/org.owasp/dependency-check-maven)
26. [Maven Checkstyle Plugin](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-checkstyle-plugin)
27. [Maven OpenAPI Generator Plugin](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin)
28. [Maven SpotBugs Plugin](https://mvnrepository.com/artifact/com.github.spotbugs/spotbugs-maven-plugin)
29. [Maven Flyway Plugin](https://mvnrepository.com/artifact/org.flywaydb/flyway-maven-plugin)
30. [Java Ngrok](https://mvnrepository.com/artifact/com.github.alexdlaird/java-ngrok)
31. [Jakarta Bean Validation API](https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api)
32. [Swagger Annotations Jakarta](https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-annotations-jakarta)
33. [Flyway Core](https://mvnrepository.com/artifact/org.flywaydb/flyway-core)
34. [Caffeine](https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine)

# TODO

Read the [TODO](TODO.md) to see the current task list.

# Useful Resources

In the creation of this pet project, I made use of these resources: [Useful](Useful.md)