package md.fuel.bot.infrastructure.repository;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.domain.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FillingStationRepositoryImpl implements FillingStationRepository {

  private static final String LATITUDE = "latitude";
  private static final String LONGITUDE = "longitude";
  private static final String RADIUS = "radius";
  private static final String LIMIT_IN_RADIUS = "limit_in_radius";
  private static final String LIMIT = "limit";
  private static final String OFFSET = "offset";

  private final WebClient webClient;
  private final FillingStationMapper mapper;
  private final String apiHost;
  private final String apiGetAll;
  private final String apiNearest;
  private final String apiBestPrice;
  private final String apiUpdateTimestamp;
  private final String apiSupportedFuelTypes;

  public FillingStationRepositoryImpl(WebClient webClient, FillingStationMapper mapper,
      @Value("${filling-station.api-host}") String apiHost,
      @Value("${filling-station.api-get-all}") String apiGetAll,
      @Value("${filling-station.api-nearest}") String apiNearest,
      @Value("${filling-station.api-best-price}") String apiBestPrice,
      @Value("${filling-station.api-update-timestamp}") String apiUpdateTimestamp,
      @Value("${filling-station.api-fuel-type}") String apiSupportedFuelTypes) {
    this.webClient = webClient;
    this.mapper = mapper;
    this.apiHost = apiHost;
    this.apiGetAll = apiGetAll;
    this.apiNearest = apiNearest;
    this.apiBestPrice = apiBestPrice;
    this.apiUpdateTimestamp = apiUpdateTimestamp;
    this.apiSupportedFuelTypes = apiSupportedFuelTypes;
  }

  @Override
  public Page<FillingStation> getAllFillingStation(double latitude, double longitude, double radius, int limitInRadius, int limit,
      int offset) {
    final String path = String.format(apiHost, apiGetAll);
    final URI uri = constructPageUri(path, latitude, longitude, radius, limitInRadius, limit, offset);

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Result<FillingStationDto>>() {
        })
        .map(f -> mapper.toEntity(f, FillingStation.class))
        .block();
  }

  @Override
  public FillingStation getNearestFillingStation(double latitude, double longitude, double radius) {
    final String path = String.format(apiHost, apiNearest);
    final URI uri = fromUriString(path)
        .queryParam(LATITUDE, latitude)
        .queryParam(LONGITUDE, longitude)
        .queryParam(RADIUS, radius)
        .queryParam(LIMIT_IN_RADIUS, 10)
        .build().toUri();

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(FillingStationDto.class)
        .map(mapper::toEntity)
        .block();
  }

  @Override
  public Page<FillingStation> getBestFuelPriceStation(double latitude, double longitude, double radius, int limitInRadius,
      int limit,
      int offset, String fuelType) {
    final String fuelTarget = String.format(apiBestPrice, fuelType);
    final String path = String.format(apiHost, fuelTarget);
    final URI uri = constructPageUri(path, latitude, longitude, radius, limitInRadius, limit, offset);

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Result<FillingStationDto>>() {
        })
        .map(f -> mapper.toEntity(f, FillingStation.class))
        .block();
  }

  @Override
  public String getUpdateTimestamp() {
    final String path = String.format(apiHost, apiUpdateTimestamp);
    final URI uri = fromUriString(path)
        .build().toUri();

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(ZonedDateTime.class)
        .map(mapper::toString)
        .block();
  }

  @Override
  public FuelType getSupportedFuelTypes() {
    final String path = String.format(apiHost, apiSupportedFuelTypes);
    final URI uri = fromUriString(path)
        .build().toUri();

    return webClient.get()
        .uri(uri)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(ArrayList.class)
        .map(mapper::toEntity)
        .block();
  }

  private URI constructPageUri(String path, double latitude, double longitude, double radius, int limitInRadius, int limit,
      int offset) {
    return fromUriString(path)
        .queryParam(LATITUDE, latitude)
        .queryParam(LONGITUDE, longitude)
        .queryParam(RADIUS, radius)
        .queryParam(LIMIT_IN_RADIUS, limitInRadius)
        .queryParam(LIMIT, limit)
        .queryParam(OFFSET, offset)
        .build().toUri();
  }
}