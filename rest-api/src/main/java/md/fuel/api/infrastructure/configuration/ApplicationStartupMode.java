package md.fuel.api.infrastructure.configuration;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.fuel.api.infrastructure.repository.AnreApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.startup.fast", havingValue = "true")
public class ApplicationStartupMode {

  private final AnreApi anreApi;

  @PostConstruct
  public void fetchFillingStationDataOnStartup() {
    anreApi.getFillingStationsInfo();
  }
}
