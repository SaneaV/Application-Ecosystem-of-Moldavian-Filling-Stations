package md.fuel.bot.infrastructure.repository;

import static java.util.Arrays.asList;
import static md.fuel.bot.infrastructure.configuration.PathUtils.resolve;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.BEST_FUEL_PRICE_PAGE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.FUEL_TYPE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.ALL_FILLING_STATIONS_PAGE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.LAST_UPDATE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.NEAREST_PATH;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.domain.Page;
import md.fuel.bot.infrastructure.configuration.ApiConfiguration;
import md.fuel.bot.infrastructure.configuration.RetryWebClientConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class FillingStationRepositoryImpl implements FillingStationRepository {

  private static final String LATITUDE = "latitude";
  private static final String LONGITUDE = "longitude";
  private static final String RADIUS = "radius";
  private static final String LIMIT_IN_RADIUS = "limit_in_radius";
  private static final String LIMIT = "limit";
  private static final String OFFSET = "offset";

  private static final List<String> GET_ALL_AND_BEST_FUEL_PRICE_REQUEST_PARAMETERS;
  private static final List<String> GET_NEAREST_REQUEST_PARAMETERS;

  static {
    GET_ALL_AND_BEST_FUEL_PRICE_REQUEST_PARAMETERS = new ArrayList<>();
    GET_NEAREST_REQUEST_PARAMETERS = new ArrayList<>();

    GET_NEAREST_REQUEST_PARAMETERS.addAll(asList(LATITUDE, LONGITUDE, RADIUS));
    GET_ALL_AND_BEST_FUEL_PRICE_REQUEST_PARAMETERS.addAll(asList(LATITUDE, LONGITUDE, RADIUS, LIMIT_IN_RADIUS, LIMIT, OFFSET));
  }

  private final WebClient webClient;
  private final FillingStationMapper mapper;
  private final RetryWebClientConfiguration retryWebClientConfiguration;
  private final ApiConfiguration apiConfiguration;

  @Override
  public Page<FillingStation> getAllFillingStation(double latitude, double longitude, double radius, int limitInRadius, int limit,
      int offset) {
    final List<Object> parameters = asList(latitude, longitude, radius, limitInRadius, limit, offset);
    final UriComponentsBuilder uriComponentsBuilder = fromUri(resolve(ALL_FILLING_STATIONS_PAGE_PATH, apiConfiguration));
    final URI uri = constructUri(uriComponentsBuilder, GET_ALL_AND_BEST_FUEL_PRICE_REQUEST_PARAMETERS, parameters);

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
    final UriComponentsBuilder uriComponentsBuilder = fromUri(resolve(NEAREST_PATH, apiConfiguration));
    final URI uri = constructUri(uriComponentsBuilder, GET_NEAREST_REQUEST_PARAMETERS, parameters);

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
    final UriComponentsBuilder uriComponentsBuilder = fromUri(resolve(BEST_FUEL_PRICE_PAGE_PATH, apiConfiguration, fuelType));
    final URI uri = constructUri(uriComponentsBuilder, GET_ALL_AND_BEST_FUEL_PRICE_REQUEST_PARAMETERS, parameters);

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
    final URI uri = fromUri(resolve(LAST_UPDATE_PATH, apiConfiguration))
        .build().toUri();

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
    final URI uri = fromUri(resolve(FUEL_TYPE_PATH, apiConfiguration))
        .build().toUri();

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(ArrayList.class)
        .retryWhen(retryWebClientConfiguration.fixedRetry())
        .map(mapper::toEntity)
        .block();
  }

  private URI constructUri(UriComponentsBuilder uriComponentsBuilder, List<String> parametersName, List<Object> parametersValue) {
    return addParameters(uriComponentsBuilder, parametersName, parametersValue)
        .build().toUri();
  }

  private UriComponentsBuilder addParameters(UriComponentsBuilder uriComponentsBuilder, List<String> parametersName,
      List<Object> parametersValue) {
    IntStream.range(0, parametersName.size())
        .forEach(i -> uriComponentsBuilder.queryParam(parametersName.get(i), parametersValue.get(i)));
    return uriComponentsBuilder;
  }
}
