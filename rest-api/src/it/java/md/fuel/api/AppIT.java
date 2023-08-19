package md.fuel.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class AppIT {

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  @DisplayName("Should start spring context")
  public void testMainMethod() {
    assertThat(applicationContext.containsBean("applicationStartupMode")).isFalse();
  }
}
