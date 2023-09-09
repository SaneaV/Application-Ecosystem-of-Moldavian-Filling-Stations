package md.fuel.api.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import md.fuel.api.rest.exception.XmlGatewayErrorWrappingStrategy;
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
@Import({XmlGatewayErrorWrappingStrategy.class})
@WebMvcTest(value = FillingStationControllerImpl.class, properties = {"app.error.strategy=XML"})
public class XmlGatewayErrorWrappingStrategyIT {

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
  private static final String RFC7807_RESPONSE = """
      {
          "Errors": {
              "Error": [
                  {
                      "source": "MD_FUEL_PRICE_APP",
                      "reasonCode": "Error reason code",
                      "description": "Error message",
                      "recoverable": false
                  }
              ]
          }
      }""";
  private static final String INVALID_LATITUDE_LONGITUDE_MESSAGE = """
      {
          "Errors": {
              "Error": [
                  {
                      "source": "MD_FUEL_PRICE_APP",
                      "reasonCode": "METHOD_ARGUMENT_NOT_VALID",
                      "description": "Longitude value should be between -90 and 90.",
                      "recoverable": false
                  },
                  {
                      "source": "MD_FUEL_PRICE_APP",
                      "reasonCode": "METHOD_ARGUMENT_NOT_VALID",
                      "description": "Latitude value should be between -90 and 90.",
                      "recoverable": false
                  }
              ]
          }
      }""";
  private static final String CONSTRAINT_EXCEPTION_MESSAGE = """
      {
          "Errors": {
              "Error": [
                  {
                      "source": "MD_FUEL_PRICE_APP",
                      "reasonCode": "CONSTRAINT_ERROR",
                      "description": "getBestFuelPrice.fuelType: The fuel type must be one of the following: Petrol, Diesel, Gas",
                      "recoverable": false
                  }
              ]
          }
      }""";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FillingStationFacade fillingStationFacade;

  @ParameterizedTest
  @MethodSource("getExceptions")
  @DisplayName("Should handle exceptions in RFC7807 format")
  void shouldHandleExceptionsInRFC7807(RuntimeException e, int status) throws Exception {
    when(fillingStationFacade.getAllFillingStations(any())).thenThrow(e);

    mockMvc.perform(get(PATH)
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(status))
        .andExpect(content().json(RFC7807_RESPONSE));

    verify(fillingStationFacade).getAllFillingStations(any());
  }

  @Test
  @DisplayName("Should throw runtime exception in XML format")
  void shouldHandleRuntimeExceptionInXMLFormat() throws Exception {
    final RuntimeException runtimeException = new RuntimeException(ERROR_MESSAGE);
    when(fillingStationFacade.getAllFillingStations(any())).thenThrow(runtimeException);

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

    verify(fillingStationFacade).getAllFillingStations(any());
  }

  @Test
  @DisplayName("Should throw method argument not valid exception in XML format")
  void shouldHandleMethodArgumentNotValidExceptionInXMLFormat() throws Exception {
    mockMvc.perform(get(PATH)
            .param(LATITUDE_PARAM, "-99")
            .param(LONGITUDE_PARAM, "99")
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST.value()))
        .andExpect(content().json(INVALID_LATITUDE_LONGITUDE_MESSAGE));
  }

  @Test
  @DisplayName("Should throw constraint violation exception  in XML format")
  void shouldHandleConstraintViolationExceptionInXMLFormat() throws Exception {
    mockMvc.perform(get("/filling-station/tip")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().is(BAD_REQUEST.value()))
        .andExpect(content().json(CONSTRAINT_EXCEPTION_MESSAGE));
  }

  private static Stream<Arguments> getExceptions() {
    return Stream.of(
        Arguments.of(new EntityNotFoundException(ERROR_MESSAGE, ERROR_REASON_CODE), NOT_FOUND.value()),
        Arguments.of(new InvalidRequestException(ERROR_MESSAGE, ERROR_REASON_CODE), BAD_REQUEST.value()),
        Arguments.of(new InfrastructureException(ERROR_MESSAGE, ERROR_REASON_CODE), BAD_REQUEST.value())
    );
  }
}
