package md.fuel.bot.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
//TODO: Check usages
public class SupportedFuelTypesFetcher {

  private final FillingStationRepository fillingStationRepository;

  @PostConstruct
  public void fetchSupportedFuelTypes() {
    log.info("Fetch supported fuel types on application startup");
    fillingStationRepository.getSupportedFuelTypes();
  }
}
