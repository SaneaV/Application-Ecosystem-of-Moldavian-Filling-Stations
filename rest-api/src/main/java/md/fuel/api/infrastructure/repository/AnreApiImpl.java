package md.fuel.api.infrastructure.repository;

import static md.fuel.api.infrastructure.configuration.PathUtils.resolve;
import static md.fuel.api.infrastructure.configuration.ResourcePath.ALL_FILLING_STATION_PATH;
import static md.fuel.api.infrastructure.configuration.ResourcePath.TODAY_FUEL_PRICE_PATH;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.infrastructure.configuration.ApiConfiguration;
import md.fuel.api.infrastructure.configuration.RetryWebClientConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.anre-stub.enabled", havingValue = "false")
public class AnreApiImpl implements AnreApi {

  private final WebClient webClient;
  private final AnreApiMapper mapper;
  private final ApiConfiguration apiConfiguration;
  private final RetryWebClientConfiguration retryWebClientConfiguration;

  @Override
  @Cacheable(value = "anreCache", cacheManager = "jCacheCacheManager")
  public List<FillingStation> getFillingStationsInfo() {
    log.info("Fetching all filling stations info from the ANRE website");
    final URI uri = resolve(ALL_FILLING_STATION_PATH, apiConfiguration);

    return webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(FillingStationApi.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(mapper::toEntity)
        .collectList()
        .block();
  }

  @Override
  @Cacheable(value = "anrePriceCache", cacheManager = "jCacheCacheManager")
  public FuelPrice getAnrePrices() {
    log.info("Fetching official ANRE prices for fuel");
    final URI uri = resolve(TODAY_FUEL_PRICE_PATH, apiConfiguration);

    return webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToMono(FuelPriceApi.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(mapper::toEntity)
        .block();
  }
}