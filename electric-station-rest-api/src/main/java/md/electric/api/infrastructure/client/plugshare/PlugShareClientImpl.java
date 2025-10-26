package md.electric.api.infrastructure.client.plugshare;

import static java.util.Objects.isNull;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.Location;
import md.electric.api.infrastructure.client.locationiq.LocationIqClient;
import md.electric.api.infrastructure.configuration.ApiConfiguration;
import md.electric.api.infrastructure.configuration.RetryWebClientConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.electric-station.plugshare.stub.enabled", havingValue = "false")
public class PlugShareClientImpl implements PlugShareClient {

  private static final String MD_CODE = "md";
  private static final String PARAM_MINIMAL = "minimal";
  private static final String PARAM_COUNT = "count";
  private static final String PARAM_LATITUDE = "latitude";
  private static final String PARAM_LONGITUDE = "longitude";
  private static final String PARAM_SPAN_LAT = "spanLat";
  private static final String PARAM_SPAN_LNG = "spanLng";
  private static final String PARAM_ACCESS = "access";

  private final PlugShareApiMapper mapper;
  private final LocationIqClient locationIqClient;
  private final WebClient webClient;
  private final ApiConfiguration apiConfig;
  private final RetryWebClientConfiguration retryWebClientConfiguration;

  @Override
  @Cacheable(value = "plugshareCache", cacheManager = "jCacheCacheManager")
  public List<ElectricStation> fetchStations(Double latitude, Double longitude) {
    return webClient.get()
        .uri(uriBuilder -> getUriBuilder(uriBuilder, latitude, longitude))
        .retrieve()
        .bodyToFlux(PlugShareResponse.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .mapNotNull(this::mapEntity)
        .collectList()
        .block();
  }

  private URI getUriBuilder(UriBuilder uriBuilder, Double latitude, Double longitude) {
    return uriBuilder
        .queryParam(PARAM_MINIMAL, 1)
        .queryParam(PARAM_COUNT, apiConfig.getCount())
        .queryParam(PARAM_LATITUDE, getOrDefault(latitude, apiConfig.getDefaultLatitude()))
        .queryParam(PARAM_LONGITUDE, getOrDefault(longitude, apiConfig.getDefaultLongitude()))
        .queryParam(PARAM_SPAN_LAT, apiConfig.getSpanLat())
        .queryParam(PARAM_SPAN_LNG, apiConfig.getSpanLng())
        .queryParam(PARAM_ACCESS, 1)
        .build();
  }

  private Double getOrDefault(Double value, Double defaultValue) {
    return isNull(value) ? defaultValue : value;
  }

  private ElectricStation mapEntity(PlugShareResponse station) {
    final Optional<Location> location =
        locationIqClient.getLocationByCoordinates(station.getLatitude(), station.getLongitude(), MD_CODE);
    return location.map(value -> mapper.toEntity(station, value))
        .orElse(null);
  }
}