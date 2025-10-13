package md.fuel.api.rest.dto;

import java.util.List;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FillingStationDtoMapper {

  FillingStationDto toDto(FillingStation fillingStation);

  List<FillingStationDto> toDto(List<FillingStation> fillingStationList);

  FuelPriceDto toDto(FuelPrice fuelPrice);

  PageDto<FillingStationDto> toDto(List<FillingStation> items, int totalResults);
}
