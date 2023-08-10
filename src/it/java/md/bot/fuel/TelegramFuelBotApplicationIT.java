package md.bot.fuel;

import static org.assertj.core.api.Assertions.assertThat;

import md.bot.fuel.configuration.NgrokWebServerTestConfiguration;
import md.bot.fuel.configuration.WebhookTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

public class TelegramFuelBotApplicationIT {

  @Nested
  @SpringBootTest(properties = "ngrok.enabled=true")
  @ActiveProfiles("test")
  @ContextConfiguration(classes = {WebhookTestConfiguration.class, NgrokWebServerTestConfiguration.class})
  public class TelegramFuelBotApplicationNgrokEnabledIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should start spring context")
    public void testMainMethod() {
      assertThat(applicationContext.containsBean("applicationStartupMode")).isFalse();
    }
  }

  @Nested
  @SpringBootTest(properties = "ngrok.enabled=false")
  @ActiveProfiles("test")
  @ContextConfiguration(classes = {WebhookTestConfiguration.class})
  public class TelegramFuelBotApplicationNgrokDisabledIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should start spring context")
    public void testMainMethod() {
      assertThat(applicationContext.containsBean("applicationStartupMode")).isFalse();
    }
  }
}
