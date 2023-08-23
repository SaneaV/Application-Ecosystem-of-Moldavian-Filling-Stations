package md.fuel.bot.infrastructure.repository;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static md.fuel.bot.infrastructure.configuration.PathUtils.resolve;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.ALL_FILLING_STATIONS_PAGE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.BEST_FUEL_PRICE_PAGE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.FUEL_TYPE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.LAST_UPDATE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.NEAREST_PATH;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.domain.Page;
import md.fuel.bot.infrastructure.configuration.ApiConfiguration;
import md.fuel.bot.infrastructure.configuration.RetryWebClientConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class FillingStationRepositoryImpl implements FillingStationRepository {

  private final WebClient webClient;
  private final FillingStationMapper mapper;
  private final RetryWebClientConfiguration retryWebClientConfiguration;
  private final ApiConfiguration apiConfiguration;

  @Override
  public Page<FillingStation> getAllFillingStation(double latitude, double longitude, double radius, int limitInRadius, int limit,
      int offset) {
    final List<Object> parameters = asList(latitude, longitude, radius, limitInRadius, limit, offset);
    final URI uri = resolve(ALL_FILLING_STATIONS_PAGE_PATH, apiConfiguration, parameters);

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Result<FillingStationDto>>() {
        })
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(f -> mapper.toEntity(f, FillingStation.class))
        .block();
  }

  @Override
  public FillingStation getNearestFillingStation(double latitude, double longitude, double radius) {
    final List<Object> parameters = asList(latitude, longitude, radius);
    final URI uri = resolve(NEAREST_PATH, apiConfiguration, parameters);

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(FillingStationDto.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(mapper::toEntity)
        .block();
  }

  @Override
  public Page<FillingStation> getBestFuelPriceStation(double latitude, double longitude, double radius, int limitInRadius,
      int limit, int offset, String fuelType) {
    final List<Object> parameters = asList(latitude, longitude, radius, limitInRadius, limit, offset);
    final URI uri = resolve(BEST_FUEL_PRICE_PAGE_PATH, apiConfiguration, parameters, fuelType);

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Result<FillingStationDto>>() {
        })
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(f -> mapper.toEntity(f, FillingStation.class))
        .block();
  }

  @Override
  public String getUpdateTimestamp() {
    final URI uri = resolve(LAST_UPDATE_PATH, apiConfiguration, emptyList());

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(ZonedDateTime.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(mapper::toString)
        .block();
  }

  @Override
  public FuelType getSupportedFuelTypes() {
    final URI uri = resolve(FUEL_TYPE_PATH, apiConfiguration, emptyList());

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(ArrayList.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(mapper::toEntity)
        .block();
  }
}
