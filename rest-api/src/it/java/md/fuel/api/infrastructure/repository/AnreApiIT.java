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

  private static final String PATH = "/";
  private static final String CONTENT_TYPE = "Content-Type";
  private static final String ANRE_RESPONSE = "[\n" +
      "    {\n" +
      "        \"x\": 2964401.691,\n" +
      "        \"y\": 6151271.452,\n" +
      "        \"sameinterval_oh\": true,\n" +
      "        \"station_type\": 3,\n" +
      "        \"station_status\": 1,\n" +
      "        \"fullstreet\": null,\n" +
      "        \"addrnum\": null,\n" +
      "        \"bua\": \"extravilan\",\n" +
      "        \"lev2\": \"Briceni\",\n" +
      "        \"lev1\": \"Criva\",\n" +
      "        \"bua_type\": null,\n" +
      "        \"cuatm_bua\": null,\n" +
      "        \"sector\": null,\n" +
      "        \"cuatm_sector\": null,\n" +
      "        \"cuatm_lev1\": 1425,\n" +
      "        \"cuatm_lev2\": 1400,\n" +
      "        \"lev1_type\": 1,\n" +
      "        \"lev2_type\": 6,\n" +
      "        \"road_side\": null,\n" +
      "        \"roadkm\": null,\n" +
      "        \"roadm\": null,\n" +
      "        \"station_name\": \"ROMPETROL\",\n" +
      "        \"idno\": \"1002600015382\",\n" +
      "        \"company_name\": \"ÎM ROMPETROL MOLDOVA SA\",\n" +
      "        \"nomenclator\": \"Peco 98\",\n" +
      "        \"property_type\": 2,\n" +
      "        \"license_bm\": \"AC 001550 din 04.06.2021\",\n" +
      "        \"license_bm_date\": \"2021-03-10\",\n" +
      "        \"license_gl\": \"AC 001551 din 04.06.2021\",\n" +
      "        \"license_gl_date\": \"2021-03-10\",\n" +
      "        \"phone\": null,\n" +
      "        \"diesel\": 21.01,\n" +
      "        \"gasoline\": 25.02,\n" +
      "        \"gpl\": 13.49,\n" +
      "        \"openhours\": \"t,24/24,24/24,24/24,24/24,24/24,24/24,24/24\"\n" +
      "    },\n" +
      "    {\n" +
      "        \"x\": 2970407.854,\n" +
      "        \"y\": 6151928.615,\n" +
      "        \"sameinterval_oh\": true,\n" +
      "        \"station_type\": 1,\n" +
      "        \"station_status\": 1,\n" +
      "        \"fullstreet\": null,\n" +
      "        \"addrnum\": null,\n" +
      "        \"bua\": \"Criva\",\n" +
      "        \"lev2\": \"Briceni\",\n" +
      "        \"lev1\": \"Criva\",\n" +
      "        \"bua_type\": 1,\n" +
      "        \"cuatm_bua\": 1425,\n" +
      "        \"sector\": null,\n" +
      "        \"cuatm_sector\": null,\n" +
      "        \"cuatm_lev1\": 1425,\n" +
      "        \"cuatm_lev2\": 1400,\n" +
      "        \"lev1_type\": 1,\n" +
      "        \"lev2_type\": 6,\n" +
      "        \"road_side\": null,\n" +
      "        \"roadkm\": 4,\n" +
      "        \"roadm\": 572,\n" +
      "        \"station_name\": \"Agro Destinație\",\n" +
      "        \"idno\": \"1014602003113\",\n" +
      "        \"company_name\": \"AGRODESTINAȚIE S.R.L.\",\n" +
      "        \"nomenclator\": \"\",\n" +
      "        \"property_type\": 1,\n" +
      "        \"license_bm\": \"AMMI 000080 din 07.04.2023\",\n" +
      "        \"license_bm_date\": \"2023-04-07\",\n" +
      "        \"license_gl\": null,\n" +
      "        \"license_gl_date\": null,\n" +
      "        \"phone\": \"69097575\",\n" +
      "        \"diesel\": 21.01,\n" +
      "        \"gasoline\": 25.02,\n" +
      "        \"gpl\": null,\n" +
      "        \"openhours\": null\n" +
      "    }\n" +
      "]";

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

    wireMock.stubFor(get(urlEqualTo(PATH)).willReturn(aResponse()
        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .withBody(ANRE_RESPONSE)));

    final List<FillingStation> fillingStationsInfo = anreApi.getFillingStationsInfo();

    assertThat(fillingStationsInfo).hasSize(2);
    assertThat(fillingStationsInfo.get(0)).usingRecursiveComparison().isEqualTo(fillingStation1);
    assertThat(fillingStationsInfo.get(1)).usingRecursiveComparison().isEqualTo(fillingStation2);
  }

  @ParameterizedTest
  @ValueSource(ints = {404, 429})
  @DisplayName("Should throw timeout exception on retry webclient configuration (worthRetrying is true)")
  void shouldThrowTimeoutExceptionOnRetryWebClientConfiguration(int status) {
    wireMock.stubFor(get(urlEqualTo(PATH))
        .willReturn(aResponse().withStatus(status)));

    assertThatThrownBy(() -> anreApi.getFillingStationsInfo())
        .isInstanceOf(InfrastructureException.class)
        .hasMessage("Request timed out.");
  }

  @Test
  @DisplayName("Should throw exception on non webclient exception (worthRetrying is false)")
  void shouldThrowExceptionOnNonWebclientException() {
    wireMock.stubFor(get(urlEqualTo(PATH))
        .willReturn(aResponse()
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody("WRONG_RESPONSE")));

    assertThatThrownBy(() -> anreApi.getFillingStationsInfo())
        .isInstanceOf(DecodingException.class)
        .hasMessage(
            "JSON decoding error: Unexpected character ('W' (code 87)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false'); nested exception is com.fasterxml.jackson.core.JsonParseException: Unexpected character ('W' (code 87)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n"
                + " at [Source: UNKNOWN; line: 1, column: 2]");
  }
}
