package md.telegram.lib.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConditionalOnMissingBean(WebClient.class)
public class DefaultWebClientConfiguration {

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .build();
  }
}