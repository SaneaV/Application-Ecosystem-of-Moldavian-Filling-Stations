package md.bot.fuel.rest.wrapper;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class FuelStationPageWrapper {

    private final FuelStationFacade fuelStationFacade;

    public PageDto<FuelStationDto> wrapAllFuelStationList(double latitude, double longitude, double radius, int limit,
                                                          int pageLimit, int offset) {
        final List<FuelStationDto> allFuelStations = fuelStationFacade.getAllFuelStations(latitude, longitude, radius, limit);
        final List<FuelStationDto> fuelStationDtos = filterFuelStations(allFuelStations, pageLimit, offset);
        return new PageDto<>(allFuelStations.size(), fuelStationDtos);
    }

    public PageDto<FuelStationDto> wrapBestFuelPrice(double latitude, double longitude, double radius, String fuelType, int limit,
                                                     int pageLimit, int offset) {
        final List<FuelStationDto> bestFuelPriceStations = fuelStationFacade.getBestFuelPrice(latitude, longitude, radius,
                fuelType, limit);
        final List<FuelStationDto> fuelStationDtos = filterFuelStations(bestFuelPriceStations, pageLimit, offset);
        return new PageDto<>(bestFuelPriceStations.size(), fuelStationDtos);
    }

    private List<FuelStationDto> filterFuelStations(List<FuelStationDto> fuelStationDtos, int pageLimit, int offset) {
        return fuelStationDtos.stream()
                .skip(offset)
                .limit(pageLimit)
                .collect(toList());
    }
}
