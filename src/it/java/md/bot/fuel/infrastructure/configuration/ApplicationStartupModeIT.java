package md.bot.fuel.infrastructure.configuration;

import md.bot.fuel.infrastructure.api.AnreApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class ApplicationStartupModeIT {

    @Nested
    @SpringBootTest(properties = {"app.startup.fast=true"}, classes = ApplicationStartupMode.class)
    class FastStartup {

        @MockBean
        private AnreApi anreApi;

        @Test
        @DisplayName("Should start application in fast mode")
        void shouldStartApplicationInFastMode() {
            when(anreApi.getFuelStationsInfo()).thenReturn(emptyList());
            verify(anreApi).getFuelStationsInfo();
        }
    }

    @Nested
    @SpringBootTest(properties = {"app.startup.fast=false"}, classes = ApplicationStartupMode.class)
    class NonFastStartup {

        @MockBean
        private AnreApi anreApi;

        @Test
        @DisplayName("Should start application in non fast mode")
        void shouldStartApplicationInNonFastMode() {
            verifyNoInteractions(anreApi);
        }
    }
}
