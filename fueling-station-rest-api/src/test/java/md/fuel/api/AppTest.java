package md.fuel.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.springframework.boot.SpringApplication;

public class AppTest {

  @Test
  @DisplayName("Should test main method via unit test")
  void testApplication() {
    try (final MockedStatic<SpringApplication> utilities = mockStatic(SpringApplication.class)) {
      utilities.when((Verification) SpringApplication.run(App.class, new String[]{})).thenReturn(null);
      App.main(new String[]{});

      assertThat(SpringApplication.run(App.class)).isEqualTo(null);
    }
  }
}