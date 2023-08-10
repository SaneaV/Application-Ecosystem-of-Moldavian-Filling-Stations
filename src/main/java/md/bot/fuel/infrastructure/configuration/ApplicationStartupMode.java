package md.bot.fuel.infrastructure.configuration;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.infrastructure.api.AnreApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.startup.fast", havingValue = "true")
public class ApplicationStartupMode {

  private final AnreApi anreApi;

  @PostConstruct
  public void fetchFuelStationInfoOnStartup() {
    anreApi.getFuelStationsInfo();
  }
}
