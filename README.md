# Moldova - Best Fuel Price Around Me - Telegram bot

<b>This bot is made for educational purposes only and is not used for commercial purposes.</b>

- [API](#api)
- [Functionality](#functionality)
- [Project Architecture](#project-architecture)
- [Installation](#installation)
- [QA](#qa)
- [Tools](#tools)
- [Useful Resources](#useful-resources)
- [Plans](#plans)

# API
To identify best fuel price and fuel station location was used: [ANRE-API](https://api.iharta.md/anre/public/)

## Functionality

User is able to specify a certain search radius and coordinates to execute the following functions:
1. Display all fuel stations (coordinates on map + all available fuel prices).
2. Display nearest fuel station (coordinates on map + all available fuel prices).
3. Display fuel station with the best \<fuel\> price (coordinates on map + \<fuel\> price).

<p align="center">
  <img src="architecture/Bot-Menu-Structure.png"  alt="Bot-Menu-Structure.png"/>
</p>

## Project Architecture

<p align="center">
  <img src="architecture/Bot-Java-Project-Structure.png"  alt="Bot-Java-Project-Structure.png"/>
</p>

## Installation

1. Download and install 
    + Program installation: [ngrok](https://ngrok.com/download)
    + Docker container: Fetch image from docker hub`docker pull ngrok/ngrok`
2. Download and install [maven](https://maven.apache.org/).
3. Create your Telegram [bot](https://telegram.me/BotFather).
4. Clone this repository to any folder on your computer.

## Usage

Program way:
1. Open ngrok.exe and write the command:

```bash
ngrok http 5000
```

Docker way:
1. Run docker image:
 
`docker run -it -e NGROK_AUTHTOKEN=xyz ngrok/ngrok:latest http host.docker.internal:80`

(*command specific for Windows and MacOS*)

`xyz` - should be your ngrok token, you can take it from: [ngrok](https://dashboard.ngrok.com/get-started/setup)

2. Copy URL with https from Forwarding line

```text
Forwarding http://27fs-299-323-0-285.ngrok.io                    
Forwarding [this URL] -> https://27fs-299-323-0-285.ngrok.io <- [this URL]  
```

3. Copy the token of your telegram bot from bot father.
4. Paste the required parameters into the link and open it in a browser:

```text
https://api.telegram.org/bot[BOT_TOKEN]/setWebhook?url=[URL_FROM_OPTION_2]
```
If everything is correct, you will see the message:
```text
{"ok":true,"result":true,"description":"Webhook was set"}
```
5. Go to the folder with this repository.
6. Open a Windows/Bash command prompt and write the command:

```bash
./mvnw spring-boot:run "-DBOT_TOKEN=[YOUR_TELEGRAM_BOT_TOKEN]" "-DWEB_HOOK_PATH=[URL_FROM_FROM_OPTION_2]"
```
7. Go to the bot and send a message `/start` message.

## QA

1. (<b>Q</b>) Why is the distance calculation in EPSG:4326? 
  - This would be faster in an environment where the bot is used by less than 700 people<sup>1</sup> per 15 minutes<sup>2</sup>.

(<b>A</b>) If the task involves web maps and interactive display of data on a plane, EPSG:3857 may be a more appropriate 
coordinate system. However, if more accurate distance measurement or working with geodetic data is required, 
EPSG:4326 may be preferable.

<sup>1</sup> - The average number of petrol stations received from ANRE: 700-800, it is more profitable to calculate 
the distance in the format received from API (EPSG:3857) and convert the user coordinates to the same coordinates 
(EPSG:3857) than to convert all petrol station coordinates to EPSG:4326 for users whose number is less than 700. When 
the number of users is more than 700-800 people, it is more favourable to convert the coordinates received from ANRE 
and calculate the distance in EPSG:4326.<br>
<sup>2</sup> - Cache retention time.

2. (<b>Q</b>) Why is there a limit of 10 petrol stations within a specified radius (Specific for Telegram only)?

(<b>A</b>) This is necessary to improve performance and also to avoid loading the system with large requests. The main 
idea of this bot to find best fuel price, in case you have more than 10 fuel stations in specified radius, it's better 
to go to the nearest one.

## Tools
1. [Telegram Bots](https://mvnrepository.com/artifact/org.telegram/telegrambots)
2. [Spring Boot](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter)
3. [H2](https://mvnrepository.com/artifact/com.h2database/h2)
4. [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)
5. [Proj4J](https://mvnrepository.com/artifact/org.locationtech.proj4j/proj4j)
6. [Proj4J-EPSG](https://mvnrepository.com/artifact/org.locationtech.proj4j/proj4j-epsg)
7. [Mapstruct](https://mvnrepository.com/artifact/org.mapstruct/mapstruct)
8. [Ehcache](https://mvnrepository.com/artifact/org.ehcache/ehcache)
9. [JSR107 API and SPI](https://mvnrepository.com/artifact/javax.cache/cache-api)

## Useful Resources
1. [Epsg.io](https://epsg.io/transform)
2. [Gis.stackexchange.com](https://gis.stackexchange.com/)
3. [Earth.google.com](https://earth.google.com/)
4. [Geleot.ru](https://geleot.ru/technology/navigation/coordinate_distance)

## Plans
1. &#9989; Release initial bot version - v1.0.0
2. &#9723; Create unit and slice tests for basic functionality - v1.0.1
3. &#9723; Create REST API - v1.1.0
4. &#9723; Create unit and slice tests for REST API - v1.1.1
5. &#9723; Configure Postgresql for project - v1.2.0
6. &#9723; Add Maven Failsafe and Surefire plugins and separate tests goals (it and test) - v1.2.1
7. &#9723; Add Jacoco Maven plugin - v1.2.2
8. &#9723; Add Dependency Check maven plugin - v1.2.3
9. &#9723; Add Dockerfile and Docker-compose to build project in container - v1.3.0
10. &#9723; Add Spring Security for REST API - v1.4.0