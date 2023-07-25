package md.bot.fuel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockedStatic.Verification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

public class TelegramFuelBotApplicationTest {

    @Nested
    class TelegramFuelBotApplicationUnitTest {

        @Test
        @DisplayName("Should test main method via unit test")
        void testApplication() {
            final MockedStatic<SpringApplication> utilities = mockStatic(SpringApplication.class);
            utilities.when((Verification) SpringApplication.run(TelegramFuelBotApplication.class, new String[]{})).thenReturn(null);
            TelegramFuelBotApplication.main(new String[]{});

            assertThat(SpringApplication.run(TelegramFuelBotApplication.class)).isEqualTo(null);
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles("test")
    class TelegramFuelBotApplicationIT {

        @Autowired
        private ApplicationContext applicationContext;

        @Test
        @DisplayName("Should start spring context")
        public void testMainMethod() {
            assertThat(applicationContext.containsBean("applicationStartupMode")).isFalse();
        }
    }
}
