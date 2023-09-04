package md.fuel.bot.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient webClient(ObjectMapper objectMapper, HttpClient httpClient,
      @Qualifier("handleServerErrorFilterFunction") ExchangeFilterFunction handleServerErrorFilterFunction) {
    return WebClient.builder()
        .exchangeStrategies(exchangeStrategies(objectMapper))
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .filter(handleServerErrorFilterFunction)
        .build();
  }

  private ExchangeStrategies exchangeStrategies(ObjectMapper objectMapper) {
    return ExchangeStrategies.builder()
        .codecs(clientCodecConfigurer -> {
          clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
          clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
        })
        .build();
  }
}
