package md.fuel.bot;

import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import md.fuel.bot.telegram.command.BestFuelInRadiusCommand;
import md.fuel.bot.telegram.configuration.SetWebhookCall;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "ngrok.enabled=false")
@ActiveProfiles("test")
public class AppIT {

  @MockBean
  private SetWebhookCall setWebhookCall;

  @MockBean
  private FillingStationRepository fillingStationRepository;

  @MockBean
  private BestFuelInRadiusCommand bestFuelInRadiusCommand;

  @Test
  @DisplayName("Should start spring context")
  public void testMainMethod() {
  }
}
