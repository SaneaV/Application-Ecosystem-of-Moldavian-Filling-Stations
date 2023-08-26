package md.fuel.api.rest.wrapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FillingStationDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FillingStationPageWrapper {

  private final FillingStationFacade fillingStationFacade;

  public PageDto<FillingStationDto> wrapAllFillingStationsList(double latitude, double longitude, double radius, int limit,
      int pageLimit, int offset) {
    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(latitude, longitude, radius,
        limit);
    final List<FillingStationDto> fillingStationDtos = filterFillingStations(allFillingStations, pageLimit, offset);
    return new PageDto<>(allFillingStations.size(), fillingStationDtos);
  }

  public PageDto<FillingStationDto> wrapBestFuelPriceStation(double latitude, double longitude, double radius, String fuelType,
      int limit, int pageLimit, int offset) {
    final List<FillingStationDto> bestFillingPriceStations = fillingStationFacade.getBestFuelPrice(latitude, longitude, radius,
        fuelType, limit);
    final List<FillingStationDto> fillingStationDtos = filterFillingStations(bestFillingPriceStations, pageLimit, offset);
    return new PageDto<>(bestFillingPriceStations.size(), fillingStationDtos);
  }

  private List<FillingStationDto> filterFillingStations(List<FillingStationDto> fillingStationDtos, int pageLimit, int offset) {
    return fillingStationDtos.stream()
        .skip(offset)
        .limit(pageLimit)
        .toList();
  }
}
