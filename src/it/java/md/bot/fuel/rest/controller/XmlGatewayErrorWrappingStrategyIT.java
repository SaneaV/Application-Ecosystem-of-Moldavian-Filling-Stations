package md.bot.fuel.rest.controller;

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
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.infrastructure.exception.ErrorWrappingStrategyFactory;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import md.bot.fuel.rest.exception.XmlGatewayErrorWrappingStrategy;
import md.bot.fuel.rest.wrapper.FuelStationPageWrapper;
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
@Import({XmlGatewayErrorWrappingStrategy.class, ErrorWrappingStrategyFactory.class})
@WebMvcTest(value = FuelStationController.class, properties = {"app.error.strategy=XML"})
public class XmlGatewayErrorWrappingStrategyIT {

  private static final String PATH = "/fuel-station";
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
      "    \"Errors\": {\n" +
      "        \"Error\": [\n" +
      "            {\n" +
      "                \"source\": \"MD_FUEL_PRICE_APP\",\n" +
      "                \"reasonCode\": \"Error reason code\",\n" +
      "                \"description\": \"Error message\",\n" +
      "                \"recoverable\": false\n" +
      "            }\n" +
      "        ]\n" +
      "    }\n" +
      "}";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FuelStationFacade fuelStationFacade;

  @MockBean
  private FuelStationPageWrapper fuelStationPageWrapper;

  @ParameterizedTest
  @MethodSource("getExceptions")
  @DisplayName("Should handle exceptions in RFC7807 format")
  void shouldHandleExceptionsInRFC7807(RuntimeException e, int status) throws Exception {
    when(fuelStationFacade.getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenThrow(e);

    mockMvc.perform(get(PATH)
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(status))
        .andExpect(content().json(RFC7807_RESPONSE));
  }

  @Test
  @DisplayName("Should throw runtime exception in RFC7807 format")
  void shouldHandleRuntimeExceptionInRFC7807Format() throws Exception {
    final RuntimeException runtimeException = new RuntimeException(ERROR_MESSAGE);
    when(fuelStationFacade.getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenThrow(runtimeException);

    final String response = "{\"Errors\":{\"Error\":[{\"source\":\"MD_FUEL_PRICE_APP\"," +
        "\"reasonCode\":\"INTERNAL_SERVER_ERROR\",\"description\":\"Error message\",\"recoverable\":false}]}}\n";

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
    final String response = "{\"Errors\":{\"Error\":[{\"source\":\"MD_FUEL_PRICE_APP\",\"reasonCode\":\"BIND_ERROR\"," +
        "\"description\":\"org.springframework.validation.BeanPropertyBindingResult: 1 errors\\nField error in object " +
        "'fuelStationRequest' on field 'radius': rejected value [RADIUS_VALUE]; codes [typeMismatch.fuelStationRequest" +
        ".radius,typeMismatch.radius,typeMismatch.double,typeMismatch]; arguments [org.springframework.context.support" +
        ".DefaultMessageSourceResolvable: codes [fuelStationRequest.radius,radius]; arguments []; default message " +
        "[radius]]; default message [Failed to convert property value of type 'java.lang.String' to required type " +
        "'double' for property 'radius'; nested exception is java.lang.NumberFormatException: For input string: " +
        "\\\"RADIUS_VALUE\\\"]\",\"recoverable\":false}]}}\n";

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
        Arguments.of(new EntityNotFoundException(ERROR_MESSAGE, ERROR_REASON_CODE), NOT_FOUND.value()),
        Arguments.of(new ExecutionException(ERROR_MESSAGE, ERROR_REASON_CODE), BAD_REQUEST.value()),
        Arguments.of(new InvalidRequestException(ERROR_MESSAGE, ERROR_REASON_CODE), BAD_REQUEST.value())
    );
  }
}
