# Telegram Bot Library

- [Manual Ngrok Installation](#manual-ngrok-installation)
- [Test](#test)
- [Plugins](#plugins)
    - [Reports](#reports)
    - [Dependency Check](#dependency-check)

<p>This library includes a basic starter for working with a Telegram bot. 
It includes setting up a Webhook, Ngrok tunnel, main interfaces and a basic mechanism for processing user messages.</p>

# Manual Ngrok Installation

+ Program installation: [ngrok](https://ngrok.com/download)

1. Open ngrok.exe and write the command:

```bash
ngrok http port
```

`port` - The port your application is running on

+ Docker container: Fetch image from docker hub `docker pull ngrok/ngrok`

1. Run docker image:

`docker run -it -e NGROK_AUTHTOKEN=xyz ngrok/ngrok:latest http host.docker.internal:port`

(*command specific for Windows and macOS*)

`xyz` - should be your ngrok token, you can take it from: [ngrok](https://dashboard.ngrok.com/get-started/setup)<br>
`port` - The port your application is running on

2. Copy URL with https from forwarding line:

```text
Forwarding http://27fs-299-323-0-285.ngrok.io                    
Forwarding [this URL] -> https://27fs-299-323-0-285.ngrok.io <- [this URL]  
```

(OPTIONAL - manual approach to set up webhook) Paste the required parameters into the link and open it in a browser:

```text
https://api.telegram.org/bot[BOT_TOKEN]/setWebhook?url=[URL_FROM_OPTION_2]
```

If everything is correct, you will see the message:

```text
{"ok":true,"result":true,"description":"Webhook was set"}
```

# Environment Variables

| **Environment Variable** |       **Optional**       | **Possible Values** | **Default Value** | **Description**                                                                                                 |
|:------------------------:|:------------------------:|:-------------------:|:-----------------:|-----------------------------------------------------------------------------------------------------------------|
|    TELEGRAM_BOT_TOKEN    |            No            | Telegram bot token  |     \<Empty\>     | Telegram bot token (you can take it from [Bot Father](https://t.me/BotFather))                                  |
|   TELEGRAM_BOT_WEBHOOK   | Depends on NGROK_ENABLED | HTTPS WebHook path  |     \<Empty\>     | HTTPS Webhook path that is connected to your telegram bot                                                       |
|    TELEGRAM_BOT_NAME     |            No            |  Telegram bot name  |     \<Empty\>     | Telegram bot name                                                                                               |
|      NGROK_ENABLED       |           Yes            |    `true/false`     |       true        | Enable/Disable automatic tunnel creation.                                                                       |
|       NGROK_TOKEN        | Depends on NGROK_ENABLED |  Ngrok auth token   |     \<Empty\>     | If NGROK_ENABLED=true, you must specify a personal ngrok [token](https://dashboard.ngrok.com/get-started/setup) |
|       SERVER_PORT        |           Yes            |       Integer       |       8080        | Embedded server (Tomcat) port for running Spring Boot application.                                              |

# Test

How to run project tests:

1. The standard way of launching via IntelliJ IDEA (from it and test packages).
2. Using maven (run from project root):
    - If you have Maven installed:
        - `mvn test` - run unit tests only
        - `mvn verify` - run unit and integration tests
    - If you don't have Maven installed:
        - `.\mvnw test` - run unit tests only
        - `.\mvnw verify` - run unit and integration tests

- Additional maven properties:
    - `-Dskip.unit.tests=true/false` - to enable/disable unit tests phase
    - `-Dskip.integration.tests=true/false` - to enable/disable integration tests phase

# Plugins

## Reports

In order to create Jacoco test coverage reports, you can run next commands:

- `mvn clean verify` - Complete application build and generate a full jacoco project report.
- Unit tests report:
    - Command: `mvn test`
    - Output directory: `target/site/jacoco-unit-tests-coverage-report/index.html`

## Dependency Check

In order to check for the presence of Vulnerable Dependencies in a project, you can use the following commands:

1. `mvn clean verify` - Complete application build and generate dependency checking report.
2. `mvn dependency-check:check` - Short command to check dependencies.

The end result can be found at: `target/dependency-check-report.html`