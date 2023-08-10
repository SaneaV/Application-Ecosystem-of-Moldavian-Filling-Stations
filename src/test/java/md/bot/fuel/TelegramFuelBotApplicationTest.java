package md.bot.fuel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

public class TelegramFuelBotApplicationTest {

  @Test
  @DisplayName("Should test main method via unit test")
  void testApplication() {
    try (final MockedStatic<SpringApplication> utilities = mockStatic(SpringApplication.class)) {
      utilities.when((MockedStatic.Verification) SpringApplication.run(TelegramFuelBotApplication.class, new String[]{}))
          .thenReturn(null);
      TelegramFuelBotApplication.main(new String[]{});

      assertThat(SpringApplication.run(TelegramFuelBotApplication.class)).isEqualTo(null);
    }
  }
}
