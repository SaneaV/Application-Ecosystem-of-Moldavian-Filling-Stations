package md.bot.fuel.facade.dto;

import md.bot.fuel.domain.FuelStation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FuelStationDtoMapper {

    FuelStationDto toDto(FuelStation fuelStation);
}
