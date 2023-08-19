package md.fuel.api.rest.controller;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.rest.exception.Rfc7807ErrorWrappingStrategy;
import md.fuel.api.rest.wrapper.FillingStationPageWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@Import({Rfc7807ErrorWrappingStrategy.class})
@WebMvcTest(value = FillingStationControllerImpl.class, properties = {"app.error.strategy=RFC7807"})
public class Rfc7807ErrorWrappingStrategyIT {

  private static final String PATH = "/filling-station";
  private static final String LATITUDE_PARAM = "latitude";
  private static final String LONGITUDE_PARAM = "longitude";
  private static final String LIMIT_IN_RADIUS_PARAM = "limit_in_radius";
  private static final String RADIUS_PARAM = "radius";

  private static final String LATITUDE_VALUE = "46.328803";
  private static final String LONGITUDE_VALUE = "28.965323";
  private static final String LIMIT_IN_RADIUS_VALUE = "10";
  private static final String RADIUS_VALUE = "5000";

  private static final String ERROR_MESSAGE = "Error message";
  private static final String ERROR_REASON_CODE = "Error reason code";
  private static final String RFC7807_RESPONSE = "{\n" +
      "    \"status\": %s,\n" +
      "    \"detail\": \"%s\",\n" +
      "    \"title\": \"%s\"\n" +
      "}";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FillingStationFacade fillingStationFacade;

  @MockBean
  private FillingStationPageWrapper fillingStationPageWrapper;

  @ParameterizedTest
  @MethodSource("getExceptions")
  @DisplayName("Should handle exceptions in RFC7807 format")
  void shouldHandleExceptionsInRFC7807(RuntimeException e, String detail, int status, String title) throws Exception {
    when(fillingStationFacade.getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenThrow(e);

    final String response = String.format(RFC7807_RESPONSE, status, detail, title);

    mockMvc.perform(get(PATH)
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(status))
        .andExpect(content().json(response));
  }

  @Test
  @DisplayName("Should throw runtime exception in RFC7807 format")
  void shouldHandleRuntimeExceptionInRFC7807Format() throws Exception {
    final RuntimeException runtimeException = new RuntimeException(ERROR_MESSAGE);
    when(fillingStationFacade.getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenThrow(runtimeException);

    final String response = "{\"status\":500,\"title\":\"Internal Server Error\",\"errorDetails\":[{\"source\":null," +
        "\"reason\":\"INTERNAL_SERVER_ERROR\",\"message\":\"Error message\",\"recoverable\":false}]}";

    mockMvc.perform(get(PATH)
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(INTERNAL_SERVER_ERROR.value()))
        .andExpect(content().json(response));
  }

  @Test
  @DisplayName("Should throw bind exception in RFC7807 format")
  void shouldHandleBindExceptionInRFC7807Format() throws Exception {
    final String response = "{\"status\":400,\"title\":\"BIND_ERROR\",\"errorDetails\":[{\"source\":\"MD_FUEL_PRICE_APP\","
        + "\"reason\":\"BIND_ERROR\",\"message\":\"Failed to convert property value of type 'java.lang.String' to required type"
        + " 'double' for property 'radius'; nested exception is java.lang.NumberFormatException: For input string: "
        + "\\\"RADIUS_VALUE\\\"\",\"recoverable\":false}]}\n";

    mockMvc.perform(get(PATH)
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, "RADIUS_VALUE")
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST.value()))
        .andExpect(content().json(response));
  }

  private static Stream<Arguments> getExceptions() {
    return Stream.of(
        Arguments.of(new EntityNotFoundException(ERROR_MESSAGE, ERROR_REASON_CODE), ERROR_MESSAGE, NOT_FOUND.value(),
            ERROR_REASON_CODE),
        Arguments.of(new InvalidRequestException(ERROR_MESSAGE, ERROR_REASON_CODE), ERROR_MESSAGE, BAD_REQUEST.value(),
            ERROR_REASON_CODE)
    );
  }
}