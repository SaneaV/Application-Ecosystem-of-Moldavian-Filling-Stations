package md.fuel.api.rest.wrapper;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FillingStationPageWrapperTest {

  private static final FillingStationDto FILLING_STATION_FORTRESS = new FillingStationDto("Fortress", 25.75, 22.3, 13.45,
      46.34746746138542, 28.947447953963454);
  private static final FillingStationDto FILLING_STATION_PETROM = new FillingStationDto("Fortress", 25.75, 22.3, 13.45,
      46.34746746138542, 28.947447953963454);

  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 20;
  private static final double RADIUS = 30;
  private static final int LIMIT = 1;
  private static final String FUEL_TYPE = "FUEL TYPE";

  private final FillingStationFacade fillingStationFacade;
  private final FillingStationPageWrapper fillingStationPageWrapper;

  public FillingStationPageWrapperTest() {
    this.fillingStationFacade = mock(FillingStationFacade.class);
    this.fillingStationPageWrapper = new FillingStationPageWrapper(fillingStationFacade);
  }

  @ParameterizedTest
  @MethodSource("getData")
  @DisplayName("Should wrap all filling stations into a page")
  void shouldWrapAllFillingStations(int pageLimit, int offset, int finalSize, List<FillingStationDto> finalList) {
    when(fillingStationFacade.getAllFillingStations(any())).thenReturn(List.of(FILLING_STATION_FORTRESS, FILLING_STATION_PETROM));

    final PageRequest pageRequest = buildPageRequest(pageLimit, offset);

    final PageDto<FillingStationDto> result = fillingStationPageWrapper.wrapAllFillingStationsList(buildLimitRequest(),
        pageRequest);

    assertThat(result.totalResults()).isEqualTo(2);
    assertThat(result.items()).hasSize(finalSize);
    assertThat(result.items()).hasSameElementsAs(finalList);
  }

  @ParameterizedTest
  @MethodSource("getData")
  @DisplayName("Should wrap best fuel price stations into a page")
  void shouldWrapBestFuelPriceStations(int pageLimit, int offset, int finalSize, List<FillingStationDto> finalList) {
    when(fillingStationFacade.getBestFuelPrice(any(), anyString())).thenReturn(
        List.of(FILLING_STATION_FORTRESS, FILLING_STATION_PETROM));

    final PageRequest pageRequest = buildPageRequest(pageLimit, offset);

    final PageDto<FillingStationDto> result = fillingStationPageWrapper.wrapBestFuelPriceStation(buildLimitRequest(), pageRequest,
        FUEL_TYPE);

    assertThat(result.totalResults()).isEqualTo(2);
    assertThat(result.items()).hasSize(finalSize);
    assertThat(result.items()).hasSameElementsAs(finalList);
  }

  private static Stream<Arguments> getData() {
    final List<FillingStationDto> allFuelStations = List.of(FILLING_STATION_FORTRESS, FILLING_STATION_PETROM);
    final List<FillingStationDto> fortressList = singletonList(FILLING_STATION_FORTRESS);
    final List<FillingStationDto> petromList = singletonList(FILLING_STATION_PETROM);

    return Stream.of(
        Arguments.of(0, 0, 0, emptyList()),
        Arguments.of(2, 0, 2, allFuelStations),
        Arguments.of(2, 1, 1, petromList),
        Arguments.of(1, 0, 1, fortressList),
        Arguments.of(2, 2, 0, emptyList())
    );
  }

  private LimitFillingStationRequest buildLimitRequest() {
    final LimitFillingStationRequest request = new LimitFillingStationRequest();
    request.setLatitude(LATITUDE);
    request.setLongitude(LONGITUDE);
    request.setRadius(RADIUS);
    request.setLimit_in_radius(LIMIT);
    request.setSorting(emptyList());
    return request;
  }

  private PageRequest buildPageRequest(int limit, int offset) {
    final PageRequest request = new PageRequest();
    request.setLimit(limit);
    request.setOffset(offset);
    return request;
  }
}
