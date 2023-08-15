package md.bot.fuel.rest.controller;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.infrastructure.exception.ErrorWrappingStrategyFactory;
import md.bot.fuel.rest.exception.Rfc7807ErrorWrappingStrategy;
import md.bot.fuel.rest.wrapper.FuelStationPageWrapper;
import md.bot.fuel.rest.wrapper.PageDto;
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
@Import({Rfc7807ErrorWrappingStrategy.class, ErrorWrappingStrategyFactory.class})
@WebMvcTest(value = FuelStationController.class)
public class FuelStationControllerImplIT {

  private static final FuelStationDto FUEL_STATION_DTO = new FuelStationDto("Fortress", 25.75, 22.3, 13.45, 46.34746746138542,
      28.947447953963454);

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

  private static final String FUEL_STATIONS_RESPONSE = "[\n" +
      "    {\n" +
      "        \"name\": \"Fortress\",\n" +
      "        \"petrol\": 25.75,\n" +
      "        \"diesel\": 22.3,\n" +
      "        \"gas\": 13.45,\n" +
      "        \"latitude\": 46.34746746138542,\n" +
      "        \"longitude\": 28.947447953963454\n" +
      "    }\n" +
      "]";
  private static final String PAGE_FUEL_STATIONS_RESPONSE = "{\n" +
      "    \"totalResults\": 1,\n" +
      "    \"items\": [\n" +
      "        {\n" +
      "            \"name\": \"Fortress\",\n" +
      "            \"petrol\": 25.75,\n" +
      "            \"diesel\": 22.3,\n" +
      "            \"gas\": 13.45,\n" +
      "            \"latitude\": 46.34746746138542,\n" +
      "            \"longitude\": 28.947447953963454\n" +
      "        }\n" +
      "    ]\n" +
      "}";
  private static final String NEAREST_FUEL_STATION_RESPONSE = "{\n" +
      "    \"name\": \"Fortress\",\n" +
      "    \"petrol\": 25.75,\n" +
      "    \"diesel\": 22.3,\n" +
      "    \"gas\": 13.45,\n" +
      "    \"latitude\": 46.34746746138542,\n" +
      "    \"longitude\": 28.947447953963454\n" +
      "}";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FuelStationFacade fuelStationFacade;

  @MockBean
  private FuelStationPageWrapper fuelStationPageWrapper;

  @Test
  @DisplayName("Should return all fuel stations in specified radius")
  void shouldReturnAllFuelStations() throws Exception {
    when(fuelStationFacade.getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt()))
        .thenReturn(singletonList(FUEL_STATION_DTO));

    mockMvc.perform(get("/fuel-station")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(FUEL_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return page of all fuel stations in specified radius")
  void shouldReturnPageOfAllFuelStations() throws Exception {
    final List<FuelStationDto> listOfFuelStations = singletonList(FUEL_STATION_DTO);
    final PageDto<FuelStationDto> pageOfFuelStations = new PageDto<>(listOfFuelStations.size(), listOfFuelStations);

    when(fuelStationPageWrapper.wrapAllFuelStationList(anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt(), anyInt()))
        .thenReturn(pageOfFuelStations);

    mockMvc.perform(get("/page/fuel-station")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .param(LIMIT_PARAM, LIMIT_VALUE)
            .param(OFFSET_PARAM, OFFSET_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(PAGE_FUEL_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return nearest fuel station in specified radius")
  void shouldReturnNearestFuelStation() throws Exception {
    when(fuelStationFacade.getNearestFuelStation(anyDouble(), anyDouble(), anyDouble())).thenReturn(FUEL_STATION_DTO);

    mockMvc.perform(get("/fuel-station/nearest")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(NEAREST_FUEL_STATION_RESPONSE));
  }

  @Test
  @DisplayName("Should return fuel station in specified radius with the best specified fuel type")
  void shouldReturnBestFuelPrice() throws Exception {
    when(fuelStationFacade.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt()))
        .thenReturn(singletonList(FUEL_STATION_DTO));

    mockMvc.perform(get("/fuel-station/gas")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(FUEL_STATIONS_RESPONSE));
  }

  @Test
  @DisplayName("Should return page with best fuel price stations")
  void shouldReturnPageWithBestFuelPrices() throws Exception {
    final List<FuelStationDto> listOfFuelStations = singletonList(FUEL_STATION_DTO);
    final PageDto<FuelStationDto> pageOfFuelStations = new PageDto<>(listOfFuelStations.size(), listOfFuelStations);
    when(fuelStationPageWrapper.wrapBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt(), anyInt(),
        anyInt())).thenReturn(pageOfFuelStations);

    mockMvc.perform(get("/page/fuel-station/gas")
            .param(LATITUDE_PARAM, LATITUDE_VALUE)
            .param(LONGITUDE_PARAM, LONGITUDE_VALUE)
            .param(LIMIT_IN_RADIUS_PARAM, LIMIT_IN_RADIUS_VALUE)
            .param(RADIUS_PARAM, RADIUS_VALUE)
            .param(LIMIT_PARAM, LIMIT_VALUE)
            .param(OFFSET_PARAM, OFFSET_VALUE)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(PAGE_FUEL_STATIONS_RESPONSE));
  }
}