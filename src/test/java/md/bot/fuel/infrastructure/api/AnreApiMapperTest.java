package md.bot.fuel.infrastructure.api;

import md.bot.fuel.domain.FuelStation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnreApiMapperTest {

    private static final String FUEL_STATION_NAME = "Fuel Station";
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
    @DisplayName("Should map fuelStationApi to fuelStation")
    void shouldMapFuelStationApiToFuelStation() {
        final FuelStationApi fuelStationApi = new FuelStationApi(FUEL_STATION_NAME, PETROL_PRICE, DIESEL_PRICE, GAS_PRICE,
                LATITUDE, LONGITUDE);

        final FuelStation result = anreApiMapper.toEntity(fuelStationApi);

        assertThat(result.getName()).isEqualTo(fuelStationApi.getName());
        assertThat(result.getPetrol()).isEqualTo(fuelStationApi.getPetrol());
        assertThat(result.getDiesel()).isEqualTo(fuelStationApi.getDiesel());
        assertThat(result.getGas()).isEqualTo(fuelStationApi.getGas());
        assertThat(result.getLongitude()).isEqualTo(fuelStationApi.getCoordinates().fst());
        assertThat(result.getLatitude()).isEqualTo(fuelStationApi.getCoordinates().snd());
    }

    @Test
    @DisplayName("Should return null on null fuelStationApi")
    void shouldReturnNullOnNullFuelStationApi() {
        final FuelStationApi fuelStationApi = null;

        final FuelStation result = anreApiMapper.toEntity(fuelStationApi);

        assertThat(result).isNull();
    }
}