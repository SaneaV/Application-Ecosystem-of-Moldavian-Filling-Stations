package md.bot.fuel.rest.wrapper;

import java.util.List;
import java.util.stream.Stream;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FuelStationPageWrapperTest {

    private static final FuelStationDto FUEL_STATION_FORTRESS = new FuelStationDto("Fortress", 25.75, 22.3,
            13.45, 46.34746746138542, 28.947447953963454);
    private static final FuelStationDto FUEL_STATION_PETROM = new FuelStationDto("Fortress", 25.75, 22.3,
            13.45, 46.34746746138542, 28.947447953963454);

    private static final double LATITUDE = 10;
    private static final double LONGITUDE = 20;
    private static final double RADIUS = 30;
    private static final int LIMIT = 1;
    private static final String FUEL_TYPE = "FUEL TYPE";

    private final FuelStationFacade fuelStationFacade;
    private final FuelStationPageWrapper fuelStationPageWrapper;

    public FuelStationPageWrapperTest() {
        this.fuelStationFacade = mock(FuelStationFacade.class);
        this.fuelStationPageWrapper = new FuelStationPageWrapper(fuelStationFacade);
    }

    @ParameterizedTest
    @MethodSource("getData")
    @DisplayName("Should wrap all fuel stations into a page")
    void shouldWrapAllFuelStations(int pageLimit, int offset, int finalSize, List<FuelStationDto> finalList) {
        when(fuelStationFacade.getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt()))
                .thenReturn(List.of(FUEL_STATION_FORTRESS, FUEL_STATION_PETROM));

        final PageDto<FuelStationDto> result = fuelStationPageWrapper.wrapAllFuelStationList(LATITUDE, LONGITUDE, RADIUS, LIMIT,
                pageLimit, offset);

        assertThat(result.getTotalResults()).isEqualTo(2);
        assertThat(result.getItems()).hasSize(finalSize);
        assertThat(result.getItems()).hasSameElementsAs(finalList);
    }

    @ParameterizedTest
    @MethodSource("getData")
    @DisplayName("Should wrap best fuel price stations into a page")
    void shouldWrapBestFuelPriceStations(int pageLimit, int offset, int finalSize, List<FuelStationDto> finalList) {
        when(fuelStationFacade.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt()))
                .thenReturn(List.of(FUEL_STATION_FORTRESS, FUEL_STATION_PETROM));

        final PageDto<FuelStationDto> result = fuelStationPageWrapper.wrapBestFuelPrice(LATITUDE, LONGITUDE, RADIUS, FUEL_TYPE,
                LIMIT, pageLimit, offset);

        assertThat(result.getTotalResults()).isEqualTo(2);
        assertThat(result.getItems()).hasSize(finalSize);
        assertThat(result.getItems()).hasSameElementsAs(finalList);
    }

    private static Stream<Arguments> getData() {
        final List<FuelStationDto> allFuelStations = List.of(FUEL_STATION_FORTRESS, FUEL_STATION_PETROM);
        final List<FuelStationDto> fortressList = singletonList(FUEL_STATION_FORTRESS);
        final List<FuelStationDto> petromList = singletonList(FUEL_STATION_PETROM);

        return Stream.of(
                Arguments.of(0, 0, 0, emptyList()),
                Arguments.of(2, 0, 2, allFuelStations),
                Arguments.of(2, 1, 1, petromList),
                Arguments.of(1, 0, 1, fortressList),
                Arguments.of(2, 2, 0, emptyList())
        );
    }
}
