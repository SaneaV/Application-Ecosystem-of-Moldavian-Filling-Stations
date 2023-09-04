package md.fuel.bot.infrastructure.repository;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.List;
import java.util.Map;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.domain.Page;
import md.fuel.bot.infrastructure.configuration.ApiConfiguration;
import md.fuel.bot.infrastructure.configuration.ApiConfigurationTest;
import md.fuel.bot.infrastructure.configuration.RetryWebClientConfiguration;
import md.fuel.bot.infrastructure.configuration.WebClientTestConfiguration;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.codec.DecodingException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({FillingStationRepositoryImpl.class, FillingStationMapperImpl.class, WebClientTestConfiguration.class,
    ApiConfigurationTest.class, RetryWebClientConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
public class FillingStationRepositoryIT {

  private static final String CONTENT_TYPE = "Content-Type";
  private static final Double LATITUDE = 46.328803;
  private static final Double LONGITUDE = 28.965323;
  private static final int LIMIT_IN_RADIUS = 10;
  private static final Double RADIUS = 5000.0;
  private static final int LIMIT = 10;
  private static final int OFFSET = 10;
  private static final Map<String, Double> PRICE_MAP = Map.of("Petrol", 25.75, "Diesel", 22.3, "Gas", 13.45);
  private static final FillingStation FILLING_STATION = new FillingStation("Fortress", PRICE_MAP, 46.34746746138542,
      28.947447953963454);

  private static final String PAGE_FILLING_STATIONS_RESPONSE = """
      {
          "totalResults": 1,
          "items": [
              {
                  "name": "Fortress",
                  "petrol": 25.75,
                  "diesel": 22.3,
                  "gas": 13.45,
                  "latitude": 46.34746746138542,
                  "longitude": 28.947447953963454
              }
          ]
      }""";
  private static final String EMPTY_PAGE_FILLING_STATIONS_RESPONSE = """
      {
          "totalResults": 0,
          "items": [
          ]
      }""";
  private static final String NULL_PAGE_FILLING_STATIONS_RESPONSE = """
      {
          "totalResults": 0
      }""";
  private static final String NEAREST_FILLING_STATION_RESPONSE = """
      {
          "name": "Fortress",
          "petrol": 25.75,
          "diesel": 22.3,
          "gas": 13.45,
          "latitude": 46.34746746138542,
          "longitude": 28.947447953963454
      }""";
  private static final String TIMESTAMP_UPDATE_RESPONSE = "\"2023-08-25T23:04:00+03:00\"";
  private static final String FUEL_TYPES_RESPONSE = "[\"Petrol\",\"Diesel\",\"Gas\"]";

  @Autowired
  private FillingStationRepository repository;

  @Autowired
  private FillingStationMapper mapper;

  @Autowired
  private WireMockServer wireMock;

  @Autowired
  private ApiConfiguration apiConfiguration;

  @Test
  @DisplayName("Should return page of filling stations")
  void shouldReturnPageOfFillingStations() {
    wireMock.stubFor(get(urlEqualTo(
        "/page/filling-station?latitude=46.328803&longitude=28.965323&radius=5000.0&limit_in_radius=10&sorting=-distance&limit=10&offset=10"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(PAGE_FILLING_STATIONS_RESPONSE)));

    final Page<FillingStation> result = repository.getAllFillingStation(LATITUDE, LONGITUDE, RADIUS, LIMIT_IN_RADIUS, LIMIT,
        OFFSET);

    assertThat(result.totalResults()).isEqualTo(1);
    assertThat(result.items().get(0)).usingRecursiveComparison().isEqualTo(FILLING_STATION);
  }

  @Test
  @DisplayName("Should return nearest filling station")
  void shouldReturnNearestFillingStation() {
    wireMock.stubFor(get(urlEqualTo(
        "/filling-station/nearest?latitude=46.328803&longitude=28.965323&radius=5000.0"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(NEAREST_FILLING_STATION_RESPONSE)));

    final FillingStation result = repository.getNearestFillingStation(LATITUDE, LONGITUDE, RADIUS);

    assertThat(result).usingRecursiveComparison().isEqualTo(FILLING_STATION);
  }

  @Test
  @DisplayName("Should return page of best fuel price filling stations")
  void shouldReturnPageOfBestFuelPriceFillingStations() {
    wireMock.stubFor(get(urlEqualTo(
        "/page/filling-station/Petrol?latitude=46.328803&longitude=28.965323&radius=5000.0&limit_in_radius=10&sorting=-distance&limit=10&offset=10"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(PAGE_FILLING_STATIONS_RESPONSE)));

    final Page<FillingStation> result = repository.getBestFuelPriceStation(LATITUDE, LONGITUDE, RADIUS, LIMIT_IN_RADIUS, LIMIT,
        OFFSET, "Petrol");

    assertThat(result.totalResults()).isEqualTo(1);
    assertThat(result.items().get(0)).usingRecursiveComparison().isEqualTo(FILLING_STATION);
  }

  @Test
  @DisplayName("Should return update timestamp")
  void shouldReturnUpdateTimestamp() {
    final String expected = "25.08.2023 20:04";

    wireMock.stubFor(get(urlEqualTo("/filling-station/last-update"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(TIMESTAMP_UPDATE_RESPONSE)));

    final String result = repository.getUpdateTimestamp();
    assertThat(result).isEqualTo(expected);
  }

  @Test
  @DisplayName("Should return supported fuel types")
  void shouldReturnSupportedFuelTypes() {
    final List<String> expected = asList("Petrol", "Diesel", "Gas");

    wireMock.stubFor(get(urlEqualTo("/filling-station/fuel-type"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(FUEL_TYPES_RESPONSE)));

    final FuelType result = repository.getSupportedFuelTypes();
    assertThat(result.fuelTypes()).containsExactlyElementsOf(expected);
  }

  @ParameterizedTest
  @ValueSource(ints = {404, 429})
  @DisplayName("Should throw timeout exception on retry webclient configuration (worthRetrying is true)")
  void shouldThrowTimeoutExceptionOnRetryWebClientConfiguration(int status) {
    wireMock.stubFor(get(urlEqualTo("/filling-station/fuel-type"))
        .willReturn(aResponse().withStatus(status)));

    assertThatThrownBy(() -> repository.getSupportedFuelTypes())
        .isInstanceOf(InfrastructureException.class)
        .hasMessage("Request timed out.");
  }

  @Test
  @DisplayName("Should throw exception on non webclient exception (worthRetrying is false)")
  void shouldThrowExceptionOnNonWebclientException() {
    wireMock.stubFor(get(urlEqualTo("/filling-station/fuel-type"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody("WRONG_RESPONSE")));

    assertThatThrownBy(() -> repository.getSupportedFuelTypes())
        .isInstanceOf(DecodingException.class)
        .hasMessage(
            "JSON decoding error: Unrecognized token 'WRONG_RESPONSE': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')");
  }

  @Test
  @DisplayName("Should return empty page on empty list")
  void shouldReturnEmptyPageOnEmptyList() {
    wireMock.stubFor(get(urlEqualTo(
        "/page/filling-station?latitude=46.328803&longitude=28.965323&radius=5000.0&limit_in_radius=10&sorting=-distance&limit=10&offset=10"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(EMPTY_PAGE_FILLING_STATIONS_RESPONSE)));

    final Page<FillingStation> result = repository.getAllFillingStation(LATITUDE, LONGITUDE, RADIUS, LIMIT_IN_RADIUS, LIMIT,
        OFFSET);

    assertThat(result.totalResults()).isEqualTo(0);
    assertThat(result.items()).hasSize(0);
  }

  @Test
  @DisplayName("Should return empty page on null list")
  void shouldReturnEmptyPageOnNullList() {
    wireMock.stubFor(get(urlEqualTo(
        "/page/filling-station?latitude=46.328803&longitude=28.965323&radius=5000.0&limit_in_radius=10&sorting=-distance&limit=10&offset=10"))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(NULL_PAGE_FILLING_STATIONS_RESPONSE)));

    final Page<FillingStation> result = repository.getAllFillingStation(LATITUDE, LONGITUDE, RADIUS, LIMIT_IN_RADIUS, LIMIT,
        OFFSET);

    assertThat(result.totalResults()).isEqualTo(0);
    assertThat(result.items()).hasSize(0);
  }
}
