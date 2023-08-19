package md.fuel.api.rest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import md.fuel.api.domain.FillingStation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FillingStationDtoMapperTest {

  private static final String FILLING_STATION_NAME = "Filling Station";
  private static final double PETROL_PRICE = 10;
  private static final double DIESEL_PRICE = 20;
  private static final double GAS_PRICE = 30;
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 10;

  private final FillingStationDtoMapper FillingStationDtoMapper;

  public FillingStationDtoMapperTest() {
    this.FillingStationDtoMapper = new FillingStationDtoMapperImpl();
  }

  @Test
  @DisplayName("Should map FillingStation to FillingStationDto")
  void shouldMapFillingStationToFillingStationDto() {
    final FillingStation FillingStation = new FillingStation(FILLING_STATION_NAME, PETROL_PRICE, DIESEL_PRICE, GAS_PRICE,
        LATITUDE, LONGITUDE);

    final FillingStationDto result = FillingStationDtoMapper.toDto(FillingStation);

    assertThat(result.getName()).isEqualTo(FillingStation.getName());
    assertThat(result.getPetrol()).isEqualTo(FillingStation.getPetrol());
    assertThat(result.getDiesel()).isEqualTo(FillingStation.getDiesel());
    assertThat(result.getGas()).isEqualTo(FillingStation.getGas());
    assertThat(result.getLatitude()).isEqualTo(FillingStation.getLatitude());
    assertThat(result.getLongitude()).isEqualTo(FillingStation.getLongitude());
  }

  @Test
  @DisplayName("Should map FillingStationDto to null on null FillingStation")
  void shouldMapFillingStationDtoToNullOnNullFillingStation() {
    final FillingStation fillingStation = null;

    final FillingStationDto result = FillingStationDtoMapper.toDto(fillingStation);

    assertThat(result).isNull();
  }
}