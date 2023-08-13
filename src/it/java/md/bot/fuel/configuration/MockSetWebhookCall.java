package md.bot.fuel.configuration;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.github.tomakehurst.wiremock.WireMockServer;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.infrastructure.configuration.WebClientConfiguration;
import md.bot.fuel.infrastructure.configuration.WebClientTestConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@RequiredArgsConstructor
@Import({WebClientConfiguration.class, WebClientTestConfiguration.class})
public class MockSetWebhookCall {

  private final WireMockServer wireMock;

  @Value("${telegram.set-webhook.endpoint}")
  private String setWebhookPath;

  @PostConstruct
  public void mockWebhookCall() {
    wireMock.stubFor(post(urlEqualTo(setWebhookPath)));
  }
}