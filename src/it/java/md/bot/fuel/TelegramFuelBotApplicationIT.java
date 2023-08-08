package md.bot.fuel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TelegramFuelBotApplicationIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should start spring context")
    public void testMainMethod() {
        assertThat(applicationContext.containsBean("applicationStartupMode")).isFalse();
    }
}
