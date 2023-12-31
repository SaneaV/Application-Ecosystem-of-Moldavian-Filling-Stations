package md.fuel.bot.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

@Configuration
public class HttpClientConfiguration {

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create();
  }
}
