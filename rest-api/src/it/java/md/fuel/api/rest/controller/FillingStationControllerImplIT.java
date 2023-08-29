package md.fuel.api.rest.controller;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import md.fuel.api.domain.FuelType;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.exception.XmlGatewayErrorWrappingStrategy;
import md.fuel.api.rest.wrapper.FillingStationPageWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@Import({XmlGatewayErrorWrappingStrategy.class})
@WebMvcTest(value = FillingStationController.class)
public class FillingStationControllerImplIT {

  private static final FillingStationDto FILLING_STATION_DTO = new FillingStationDto("Fortress", 25.75, 22.3, 13.45,
      46.34746746138542, 28.947447953963454);

  private static final String LATITUDE_PARAM = "latitude";
  private static final String LONGITUDE_PARAM = "longitude";
  private static final String LIMIT_IN_RADIUS_PARAM = "limit_in_radius";
  private static final String RADIUS_PARAM = "radius";
  private static final String LIMIT_PARAM = "limit";
  private static final String OFFSET_PARAM = "offset";

  private static final String LATITUDE_VALUE = "46.328803";
  private static final String LONGITUDE_VALUE = "28.965323";
  private static final String LIMIT_IN_RADIUS_VALUE = "10";
  private static final String RADIUS_VALUE = "5000";
  private static final String LIMIT_VALUE = "20";
  private static final String OFFSET_VALUE = "0";

  private static final String FILLING_STATIONS_RESPONSE = """
      [
          {
              "name": "Fortress",
              "petrol": 25.75,
              "diesel": 22.3,
              "gas": 13.45,
              "latitude": 46.34746746138542,
              "longitude": 28.947447953963454
          }
      ]""";
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
  private static final String NEAREST_FILLING_STATION_RESPONSE = """
      {
          "name": "Fortress",
          "petrol": 25.75,
          "diesel": 22.3,
          "gas": 13.45,
          "latitude": 46.34746746138542,
          "longitude": 28.947447953963454
      }""";
  private static final String FUEL_TYPES_RESPONSE = "[\"Petrol\",\"Diesel\",\"Gas\"]";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FillingStationFacade fillingStationFacade;

  @MockBean
  private FillingStationPageWrapper fillingStationPageWrapper;

  @Test
  @DisplayName("Should return all filling stations in specified radius")
  void shouldReturnAllFillingStations() throws Exception {
    when(fillingStationFacade.getAllFillingStations(any())).thenReturn(singletonList(FILLING_STATION_DTO));

    mockMvc.perform(get("/filling-station")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(FILLING_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return page of all filling stations in specified radius")
  void shouldReturnPageOfAllFillingStations() throws Exception {
    final List<FillingStationDto> listOfFillingStations = singletonList(FILLING_STATION_DTO);
    final PageDto<FillingStationDto> pageOfFillingStations = new PageDto<>(listOfFillingStations.size(), listOfFillingStations);

    when(fillingStationPageWrapper.wrapAllFillingStationsList(any(), any())).thenReturn(pageOfFillingStations);

    mockMvc.perform(get("/page/filling-station")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .param(LIMIT_PARAM, LIMIT_VALUE)
            .param(OFFSET_PARAM, OFFSET_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(PAGE_FILLING_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return nearest filling station in specified radius")
  void shouldReturnNearestFillingStation() throws Exception {
    when(fillingStationFacade.getNearestFillingStation(any())).thenReturn(FILLING_STATION_DTO);

    mockMvc.perform(get("/filling-station/nearest")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(NEAREST_FILLING_STATION_RESPONSE));
  }

  @Test
  @DisplayName("Should return filling station in specified radius with the best specified fuel type")
  void shouldReturnBestFuelPrice() throws Exception {
    when(fillingStationFacade.getBestFuelPrice(any(), anyString())).thenReturn(singletonList(FILLING_STATION_DTO));

    mockMvc.perform(get("/filling-station/gas")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(FILLING_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return page with best fuel price stations")
  void shouldReturnPageWithBestFuelPrices() throws Exception {
    final List<FillingStationDto> listOfFillingStations = singletonList(FILLING_STATION_DTO);
    final PageDto<FillingStationDto> pageOfFillingStations = new PageDto<>(listOfFillingStations.size(), listOfFillingStations);
    when(
        fillingStationPageWrapper.wrapBestFuelPriceStation(any(), any(), anyString())).thenReturn(pageOfFillingStations);

    mockMvc.perform(get("/page/filling-station/gas")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .param(LIMIT_PARAM, LIMIT_VALUE)
            .param(OFFSET_PARAM, OFFSET_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(PAGE_FILLING_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return last update timestamp")
  void shouldReturnLastUpdateTimestamp() throws Exception {
    final String timestampString = "2023-08-25T13:32:03.1907587+03:00";
    final ZonedDateTime timestamp = ZonedDateTime.parse(timestampString);

    when(fillingStationFacade.getLastUpdateTimestamp()).thenReturn(timestamp);

    mockMvc.perform(get("/filling-station/last-update")
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("\"" + timestampString + "\""));
  }

  @Test
  @DisplayName("Should return list of fuel types")
  void shouldReturnListOfFuelTypes() throws Exception {
    final List<String> fuelTypes = Arrays.stream(FuelType.values())
        .map(FuelType::getDescription)
        .toList();

    when(fillingStationFacade.getAvailableFuelTypes()).thenReturn(fuelTypes);

    mockMvc.perform(get("/filling-station/fuel-type")
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(FUEL_TYPES_RESPONSE));
  }
}