package md.fuel.api.rest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FuelPriceDto;
import md.fuel.api.rest.exception.XmlGatewayErrorWrappingStrategy;
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
@WebMvcTest(value = AnrePriceControllerImpl.class)
public class AnrePriceControllerIT {

  private static final String ANRE_FUEL_PRICE = """
      {
          "petrol": 10.0,
          "diesel": 10.0,
          "date": "%s"
      }""";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FillingStationFacade fillingStationFacade;

  @Test
  @DisplayName("Should return all filling stations in specified radius")
  void shouldReturnAllFillingStations() throws Exception {
    final String date = LocalDate.now().toString();
    final FuelPriceDto fuelPriceDto = new FuelPriceDto(date, 10.0, 10.0);
    when(fillingStationFacade.getAnrePrices()).thenReturn(fuelPriceDto);

    mockMvc.perform(get("/price")
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(String.format(ANRE_FUEL_PRICE, date)));
  }
}