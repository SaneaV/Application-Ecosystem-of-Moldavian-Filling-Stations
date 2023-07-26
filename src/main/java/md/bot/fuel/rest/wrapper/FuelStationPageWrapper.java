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

    public PageDto<FuelStationDto> wrapAllFuelStationList(double userLatitude, double userLongitude, double userRadius,
                                                          int limit, int pageLimit, int offset) {
        final List<FuelStationDto> allFuelStations = fuelStationFacade.getAllFuelStations(userLatitude, userLongitude,
                userRadius, limit);
        final List<FuelStationDto> fuelStationDtos = filterFuelStations(allFuelStations, pageLimit, offset);

        return new PageDto<>(allFuelStations.size(), fuelStationDtos);
    }

    public PageDto<FuelStationDto> wrapBestFuelPrice(double userLatitude, double userLongitude, double userRadius,
                                                     String fuelType, int limit, int pageLimit, int offset) {
        final List<FuelStationDto> bestFuelPriceStations = fuelStationFacade.getBestFuelPrice(userLatitude, userLongitude,
                userRadius, fuelType, limit);
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
