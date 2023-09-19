package md.fuel.api.infrastructure.repository;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.infrastructure.configuration.ApiConfigurationTest;
import md.fuel.api.infrastructure.configuration.RetryWebClientConfiguration;
import md.fuel.api.infrastructure.configuration.WebClientTestConfiguration;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
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
@Import({AnreApiImpl.class, AnreApiMapperImpl.class, WebClientTestConfiguration.class, ApiConfigurationTest.class,
    RetryWebClientConfiguration.class})
@TestPropertySource("classpath:application-test.properties")
public class AnreApiIT {

  private static final String ALL_FILLING_STATIONS_PATH = "/";
  private static final String ANRE_PRICES_PATH = "/plafon";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String ANRE_RESPONSE = """
      [
          {
              "x": 2964401.691,
              "y": 6151271.452,
              "sameinterval_oh": true,
              "station_type": 3,
              "station_status": 1,
              "fullstreet": null,
              "addrnum": null,
              "bua": "extravilan",
              "lev2": "Briceni",
              "lev1": "Criva",
              "bua_type": null,
              "cuatm_bua": null,
              "sector": null,
              "cuatm_sector": null,
              "cuatm_lev1": 1425,
              "cuatm_lev2": 1400,
              "lev1_type": 1,
              "lev2_type": 6,
              "road_side": null,
              "roadkm": null,
              "roadm": null,
              "station_name": "ROMPETROL",
              "idno": "1002600015382",
              "company_name": "ÎM ROMPETROL MOLDOVA SA",
              "nomenclator": "Peco 98",
              "property_type": 2,
              "license_bm": "AC 001550 din 04.06.2021",
              "license_bm_date": "2021-03-10",
              "license_gl": "AC 001551 din 04.06.2021",
              "license_gl_date": "2021-03-10",
              "phone": null,
              "diesel": 21.01,
              "gasoline": 25.02,
              "gpl": 13.49,
              "openhours": "t,24/24,24/24,24/24,24/24,24/24,24/24,24/24"
          },
          {
              "x": 2970407.854,
              "y": 6151928.615,
              "sameinterval_oh": true,
              "station_type": 1,
              "station_status": 1,
              "fullstreet": null,
              "addrnum": null,
              "bua": "Criva",
              "lev2": "Briceni",
              "lev1": "Criva",
              "bua_type": 1,
              "cuatm_bua": 1425,
              "sector": null,
              "cuatm_sector": null,
              "cuatm_lev1": 1425,
              "cuatm_lev2": 1400,
              "lev1_type": 1,
              "lev2_type": 6,
              "road_side": null,
              "roadkm": 4,
              "roadm": 572,
              "station_name": "Agro Destinație",
              "idno": "1014602003113",
              "company_name": "AGRODESTINAȚIE S.R.L.",
              "nomenclator": "",
              "property_type": 1,
              "license_bm": "AMMI 000080 din 07.04.2023",
              "license_bm_date": "2023-04-07",
              "license_gl": null,
              "license_gl_date": null,
              "phone": "69097575",
              "diesel": 21.01,
              "gasoline": 25.02,
              "gpl": null,
              "openhours": null
          }
      ]""";
  private static final String ANRE_OFFICIAL_PRICES_RESPONSE = """
      {"date":"2023-09-19","b_pc":26.76,"m_pc":24.5}""";

  @Autowired
  private AnreApi anreApi;

  @Autowired
  private AnreApiMapper mapper;

  @Autowired
  private WireMockServer wireMock;

  @Test
  @DisplayName("Should return list of filling stations")
  void shouldReturnListOfFillingStation() {
    final FillingStation fillingStation1 = new FillingStation("ROMPETROL", 25.02, 21.01, 13.49, 48.26629334102463,
        26.62967347295055);
    final FillingStation fillingStation2 = new FillingStation("Agro Destinație", 25.02, 21.01, null, 48.27022290028225,
        26.68362775316868);

    wireMock.stubFor(get(urlEqualTo(ALL_FILLING_STATIONS_PATH)).willReturn(aResponse()
        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .withBody(ANRE_RESPONSE)));

    final List<FillingStation> fillingStationsInfo = anreApi.getFillingStationsInfo();

    assertThat(fillingStationsInfo).hasSize(2);
    assertThat(fillingStationsInfo.get(0)).usingRecursiveComparison().isEqualTo(fillingStation1);
    assertThat(fillingStationsInfo.get(1)).usingRecursiveComparison().isEqualTo(fillingStation2);
  }

  @Test
  @DisplayName("Should return ANRE official prices")
  void shouldReturnAnreOfficialPrices() {
    wireMock.stubFor(get(urlEqualTo(ANRE_PRICES_PATH)).willReturn(aResponse()
        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .withBody(ANRE_OFFICIAL_PRICES_RESPONSE)));

    final FuelPrice result = anreApi.getAnrePrices();

    assertThat(result.petrol()).isEqualTo(26.76);
    assertThat(result.diesel()).isEqualTo(24.5);
    assertThat(result.date()).isEqualTo("2023-09-19");
  }

  @ParameterizedTest
  @ValueSource(ints = {404, 429})
  @DisplayName("Should throw timeout exception on retry webclient configuration (worthRetrying is true)")
  void shouldThrowTimeoutExceptionOnRetryWebClientConfiguration(int status) {
    wireMock.stubFor(get(urlEqualTo(ALL_FILLING_STATIONS_PATH))
        .willReturn(aResponse().withStatus(status)));

    assertThatThrownBy(() -> anreApi.getFillingStationsInfo())
        .isInstanceOf(InfrastructureException.class)
        .hasMessage("Request timed out.");
  }

  @Test
  @DisplayName("Should throw exception on non webclient exception (worthRetrying is false)")
  void shouldThrowExceptionOnNonWebclientException() {
    wireMock.stubFor(get(urlEqualTo(ALL_FILLING_STATIONS_PATH))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody("WRONG_RESPONSE")));

    assertThatThrownBy(() -> anreApi.getFillingStationsInfo())
        .isInstanceOf(DecodingException.class)
        .hasMessage(
            "JSON decoding error: Unexpected character ('W' (code 87)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false')");
  }
}