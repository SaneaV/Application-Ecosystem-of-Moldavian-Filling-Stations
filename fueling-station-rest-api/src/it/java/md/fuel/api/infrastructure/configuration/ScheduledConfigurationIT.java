package md.fuel.api.infrastructure.configuration;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import md.fuel.api.infrastructure.client.AnreApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ScheduledConfigurationIT {

  @Nested
  @SpringBootTest(properties = {"app.scheduled-fetching.enabled=true"}, classes = ScheduledConfiguration.class)
  class FastStartup {

    @MockBean
    private AnreApi anreApi;

    @Test
    @DisplayName("Should start application in fast mode")
    void shouldStartApplicationInFastMode() {
      when(anreApi.getFillingStationsInfo()).thenReturn(emptyList());
      verify(anreApi).getFillingStationsInfo();
    }
  }

  @Nested
  @SpringBootTest(properties = {"app.scheduled-fetching.enabled=false"}, classes = ScheduledConfiguration.class)
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
