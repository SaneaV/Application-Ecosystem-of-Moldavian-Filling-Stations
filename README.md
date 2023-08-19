# Application Ecosystem of Moldavian Filling Stations

<b>This application ecosystem is made for educational purposes only and is not used for commercial purposes.</b>

<p>This pet project was created to summarize the knowledge I have gained over 2+ years of Java development. I used all my skills 
in building application architecture, customizing third-party tools and libraries and much more.</p>

- [API](#api)
- [Structure](#structure)
- [Tools](#tools)
- [TODO](#todo)

# API

To identify the best fuel price, and filling station location was used: [ANRE-API](https://api.iharta.md/anre/public/)

# Structure

- [Contract-Generator](/contract-generator) - is library used in the bundle between Rest-API and Telegram-Bot, Java classes are
  generated using Swagger OpenAPI generator.
- [REST-API](/rest-api) - is a web service offering a set of REST APIs for working with filling stations in Moldova.
- [Telegram-Bot](/telegram-bot) - is a bot that uses Telegram messenger for comfortable search of information about locations and
  prices of Moldovan filling stations.

# Tools

1. [Telegram Bots Spring Boot Starter](https://mvnrepository.com/artifact/org.telegram/telegrambots-spring-boot-starter)
2. [Spring Boot Starter](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter)
3. [Spring Boot Starter Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation)
4. [Spring Boot Starter Cache](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache)
5. [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
6. [Spring Boot Starter WebFlux](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webflux)
7. [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
8. [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test)
9. [SpringDoc OpenAPI UI](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui)
10. [H2](https://mvnrepository.com/artifact/com.h2database/h2)
11. [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
12. [Proj4J](https://mvnrepository.com/artifact/org.locationtech.proj4j/proj4j)
13. [Proj4J-EPSG](https://mvnrepository.com/artifact/org.locationtech.proj4j/proj4j-epsg)
14. [Mapstruct](https://mvnrepository.com/artifact/org.mapstruct/mapstruct)
15. [Ehcache](https://mvnrepository.com/artifact/org.ehcache/ehcache)
16. [JSR107 API and SPI](https://mvnrepository.com/artifact/javax.cache/cache-api)
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
27. [Maven OpenAPI Generator](https://mvnrepository.com/artifact/org.openapitools/openapi-generator-maven-plugin)
28. [Java Ngrok](https://mvnrepository.com/artifact/com.github.alexdlaird/java-ngrok)
29. [Swagger Core](https://mvnrepository.com/artifact/io.swagger.core.v3/swagger-core)
30. [Swagger UI](https://mvnrepository.com/artifact/org.webjars/swagger-ui)

# TODO

Read the [TODO](TODO.md) to see the current task list.