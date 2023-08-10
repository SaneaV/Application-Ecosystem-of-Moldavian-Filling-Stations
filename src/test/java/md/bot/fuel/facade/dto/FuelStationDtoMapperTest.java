package md.bot.fuel.facade.dto;

import static org.assertj.core.api.Assertions.assertThat;

import md.bot.fuel.domain.FuelStation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FuelStationDtoMapperTest {

  private static final String FUEL_STATION_NAME = "Fuel Station";
  private static final double PETROL_PRICE = 10;
  private static final double DIESEL_PRICE = 20;
  private static final double GAS_PRICE = 30;
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 10;

  private final FuelStationDtoMapper fuelStationDtoMapper;

  public FuelStationDtoMapperTest() {
    this.fuelStationDtoMapper = new FuelStationDtoMapperImpl();
  }

  @Test
  @DisplayName("Should map fuelStation to fuelStationDto")
  void shouldMapFuelStationToFuelStationDto() {
    final FuelStation fuelStation = new FuelStation(FUEL_STATION_NAME, PETROL_PRICE, DIESEL_PRICE, GAS_PRICE, LATITUDE,
        LONGITUDE);

    final FuelStationDto result = fuelStationDtoMapper.toDto(fuelStation);

    assertThat(result.getName()).isEqualTo(fuelStation.getName());
    assertThat(result.getPetrol()).isEqualTo(fuelStation.getPetrol());
    assertThat(result.getDiesel()).isEqualTo(fuelStation.getDiesel());
    assertThat(result.getGas()).isEqualTo(fuelStation.getGas());
    assertThat(result.getLatitude()).isEqualTo(fuelStation.getLatitude());
    assertThat(result.getLongitude()).isEqualTo(fuelStation.getLongitude());
  }

  @Test
  @DisplayName("Should map fuelStationDto to null on null fuelStation")
  void shouldMapFuelStationDtoToNullOnNullFuelStation() {
    final FuelStation fuelStation = null;

    final FuelStationDto result = fuelStationDtoMapper.toDto(fuelStation);

    assertThat(result).isNull();
  }
}