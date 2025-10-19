package md.electric.api.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String ACCEPT_HEADER = "accept";
  private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
  private static final String CACHE_CONTROL_HEADER = "Cache-Control";
  private static final String PRAGMA_HEADER = "Pragma";
  private static final String USER_AGENT_HEADER = "User-Agent";

  private final ApiConfiguration apiConfig;

  @Bean
  public WebClient webClient(ObjectMapper objectMapper, HttpClient httpClient) {
    log.info("Initialize web client");
    return WebClient.builder()
        .baseUrl(apiConfig.getBaseUrl())
        .defaultHeader(AUTHORIZATION_HEADER, apiConfig.getAuthorization())
        .defaultHeader(ACCEPT_HEADER, apiConfig.getAccept())
        .defaultHeader(ACCEPT_LANGUAGE_HEADER, apiConfig.getAcceptLanguage())
        .defaultHeader(CACHE_CONTROL_HEADER, apiConfig.getCacheControl())
        .defaultHeader(PRAGMA_HEADER, apiConfig.getPragma())
        .defaultHeader(USER_AGENT_HEADER, apiConfig.getUserAgent())
        .exchangeStrategies(exchangeStrategies(objectMapper))
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }

  private ExchangeStrategies exchangeStrategies(ObjectMapper objectMapper) {
    log.info("Create exchange strategies with jackson2JsonDecoder and jackson2JsonEncoder");
    return ExchangeStrategies.builder()
        .codecs(clientCodecConfigurer -> {
          clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
          clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
        })
        .build();
  }
}