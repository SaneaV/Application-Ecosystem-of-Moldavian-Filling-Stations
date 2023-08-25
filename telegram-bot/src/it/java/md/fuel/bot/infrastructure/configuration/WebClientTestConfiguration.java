package md.fuel.bot.infrastructure.configuration;

import static org.springframework.test.util.TestSocketUtils.findAvailableTcpPort;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class WebClientTestConfiguration {

  @Bean
  public Integer getAvailableTcpPort() {
    return findAvailableTcpPort();
  }

  @Bean
  public WireMockServer webServer(Integer availableTcpPort) {
    final WireMockServer wireMockServer = new WireMockServer(availableTcpPort );
    wireMockServer.start();
    return wireMockServer;
  }

  @Bean
  public WebClient webClient(WireMockServer server) {
    return WebClient.builder()
        .baseUrl(server.baseUrl())
        .build();
  }
}
