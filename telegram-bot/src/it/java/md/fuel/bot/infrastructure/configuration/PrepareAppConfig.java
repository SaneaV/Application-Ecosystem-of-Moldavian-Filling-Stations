package md.fuel.bot.infrastructure.configuration;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import com.github.tomakehurst.wiremock.WireMockServer;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@RequiredArgsConstructor
@Import({WebClientConfiguration.class, WebClientTestConfiguration.class})
public class PrepareAppConfig {

  private final WireMockServer wireMock;
  @MockBean
  private final FillingStationRepository repository;

  @Value("${telegram.set-webhook.endpoint}")
  private String setWebhookPath;

  @PostConstruct
  public void mockWebhookCall() {
    when(repository.getSupportedFuelTypes()).thenReturn(new FuelType(asList("Petrol", "Diesel", "Gas")));
    wireMock.stubFor(post(urlEqualTo(setWebhookPath)));
  }
}