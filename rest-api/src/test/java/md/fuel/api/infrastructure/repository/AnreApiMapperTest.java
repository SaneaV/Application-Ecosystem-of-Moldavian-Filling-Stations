package md.fuel.api.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;

import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnreApiMapperTest {

  private static final String FILLING_STATION_NAME = "Filling Station";
  private static final double PETROL_PRICE = 10;
  private static final double DIESEL_PRICE = 20;
  private static final double GAS_PRICE = 30;
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 10;

  private final AnreApiMapper anreApiMapper;

  public AnreApiMapperTest() {
    this.anreApiMapper = new AnreApiMapperImpl();
  }

  @Test
  @DisplayName("Should map fillingStationApi to fuelStation")
  void shouldMapFillingStationApiToFillingStation() {
    final FillingStationApi fillingStationApi = new FillingStationApi(FILLING_STATION_NAME, PETROL_PRICE, DIESEL_PRICE, GAS_PRICE,
        LATITUDE, LONGITUDE);

    final FillingStation result = anreApiMapper.toEntity(fillingStationApi);

    assertThat(result.name()).isEqualTo(fillingStationApi.getName());
    assertThat(result.petrol()).isEqualTo(fillingStationApi.getPetrol());
    assertThat(result.diesel()).isEqualTo(fillingStationApi.getDiesel());
    assertThat(result.gas()).isEqualTo(fillingStationApi.getGas());
    assertThat(result.longitude()).isEqualTo(fillingStationApi.getCoordinates().fst());
    assertThat(result.latitude()).isEqualTo(fillingStationApi.getCoordinates().snd());
  }

  @Test
  @DisplayName("Should return null on null fillingStationApi")
  void shouldReturnNullOnNullFillingStationApi() {
    final FillingStation result = anreApiMapper.toEntity((FillingStationApi) null);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should map zero or null price to null")
  void shouldMapZeroOrNullPriceToNull() {
    final FillingStationApi fillingStationApi = new FillingStationApi(FILLING_STATION_NAME, 0.0, null, 0.0D, LATITUDE, LONGITUDE);

    final FillingStation result = anreApiMapper.toEntity(fillingStationApi);

    assertThat(result.name()).isEqualTo(fillingStationApi.getName());
    assertThat(result.petrol()).isNull();
    assertThat(result.diesel()).isNull();
    assertThat(result.gas()).isNull();
    assertThat(result.longitude()).isEqualTo(fillingStationApi.getCoordinates().fst());
    assertThat(result.latitude()).isEqualTo(fillingStationApi.getCoordinates().snd());
  }

  @Test
  @DisplayName("Should return null on null FuelPriceApi")
  void shouldReturnNullOnNullFuelPriceApi() {
    final FuelPrice result = anreApiMapper.toEntity((FuelPriceApi) null);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should map FuelPriceApi to FuelPriceApi")
  void shouldMapFuelPriceApiToFuelPrice() {
    final FuelPriceApi fuelPrice = new FuelPriceApi(PETROL_PRICE, DIESEL_PRICE, LOCAL_DATE.toString());

    final FuelPrice result = anreApiMapper.toEntity(fuelPrice);

    assertThat(result.petrol()).isEqualTo(fuelPrice.getPetrol());
    assertThat(result.diesel()).isEqualTo(fuelPrice.getDiesel());
    assertThat(result.date()).isEqualTo(fuelPrice.getDate());
  }
}