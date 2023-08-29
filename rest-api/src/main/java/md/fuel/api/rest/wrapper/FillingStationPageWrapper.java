package md.fuel.api.rest.wrapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.facade.FillingStationFacade;
import md.fuel.api.rest.dto.FillingStationDto;
import md.fuel.api.rest.dto.PageDto;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FillingStationPageWrapper {

  private final FillingStationFacade fillingStationFacade;

  public PageDto<FillingStationDto> wrapAllFillingStationsList(LimitFillingStationRequest fillingStationRequest,
      PageRequest pageRequest) {
    final List<FillingStationDto> allFillingStations = fillingStationFacade.getAllFillingStations(fillingStationRequest);
    final List<FillingStationDto> fillingStationDtos = filterFillingStations(allFillingStations, pageRequest.getLimit(),
        pageRequest.getOffset());
    return new PageDto<>(allFillingStations.size(), fillingStationDtos);
  }

  public PageDto<FillingStationDto> wrapBestFuelPriceStation(LimitFillingStationRequest fillingStationRequest,
      PageRequest pageRequest, String fuelType) {
    final List<FillingStationDto> bestFillingPriceStations = fillingStationFacade.getBestFuelPrice(fillingStationRequest,
        fuelType);
    final List<FillingStationDto> fillingStationDtos = filterFillingStations(bestFillingPriceStations, pageRequest.getLimit(),
        pageRequest.getOffset());
    return new PageDto<>(bestFillingPriceStations.size(), fillingStationDtos);
  }

  private List<FillingStationDto> filterFillingStations(List<FillingStationDto> fillingStationDtos, int pageLimit, int offset) {
    return fillingStationDtos.stream()
        .skip(offset)
        .limit(pageLimit)
        .toList();
  }
}
