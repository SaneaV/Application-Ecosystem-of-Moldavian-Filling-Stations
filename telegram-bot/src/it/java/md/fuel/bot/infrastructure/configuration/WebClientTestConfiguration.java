package md.fuel.bot.infrastructure.configuration;

import static org.springframework.test.util.TestSocketUtils.findAvailableTcpPort;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Import(WebClientErrorHandlingFilterFunctionConfig.class)
@TestConfiguration
public class WebClientTestConfiguration {

  @Bean
  public Integer getAvailableTcpPort() {
    return findAvailableTcpPort();
  }

  @Bean
  public WireMockServer webServer(Integer availableTcpPort) {
    final WireMockServer wireMockServer = new WireMockServer(availableTcpPort);
    wireMockServer.start();
    return wireMockServer;
  }

  @Bean
  public WebClient webClient(WireMockServer server,
      @Qualifier("handleServerErrorFilterFunction") ExchangeFilterFunction handleServerErrorFilterFunction) {
    return WebClient.builder()
        .baseUrl(server.baseUrl())
        .filter(handleServerErrorFilterFunction)
        .build();
  }
}
