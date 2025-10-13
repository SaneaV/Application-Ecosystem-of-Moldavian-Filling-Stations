# Application Ecosystem of Moldavian Filling Stations

<b>This application ecosystem is made for educational purposes only and is not used for commercial purposes.</b>

<p>This pet project was created to summarize the knowledge I have gained over 2+ years of Java development. I used all my skills 
in building application architecture, customizing third-party tools and libraries and much more.</p>

- [API](#api)
- [Structure](#structure)
- [TODO](#todo)
- [Useful Resources](#useful-resources)

# API

To identify the best fuel price, and filling station location was used: [ANRE-API](https://api.iharta.md/anre/public/)

# Structure

- [Contract-Generator](/contract-generator) - is a library used in the bundle between Rest-API and Telegram-Bot; Java classes are
  generated using Swagger OpenAPI generator.
- [Report-Aggregate](/report-aggregate) - is a module that allows to aggregate Jacoco test coverage report data.
- [FUELING-STATIION-REST-API](/fueling-station-rest-api) - is a web service offering a set of REST APIs for working with filling stations in Moldova.
- [Telegram-Bot](/telegram-bot) - is a bot that uses Telegram messenger for comfortable search of information about locations and
  prices of Moldovan filling stations.
- [Telegram-Bot-Lib](/telegram-bot-lib) - A library that includes basic components and configurations for running a Telegram bot.
- [Cache-Lib](/cache-lib) - A library that provides a caching mechanism in external services.

# TODO

Read the [TODO](TODO.md) to see the current task list.

# Useful Resources

In the creation of this pet project, I made use of these resources: [Useful](Useful.md)