package md.fuel.bot.infrastructure.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.repository.FillingStationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportedFuelTypesFetcher {

  private final FillingStationRepository fillingStationRepository;

  @PostConstruct
  public void fetchSupportedFuelTypes() {
    fillingStationRepository.getSupportedFuelTypes();
  }
}
