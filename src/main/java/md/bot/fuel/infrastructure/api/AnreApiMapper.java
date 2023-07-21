package md.bot.fuel.infrastructure.api;

import md.bot.fuel.domain.FuelStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnreApiMapper {

    @Mapping(target = "longitude", expression = "java(fuelStationApi.getCoordinates().fst())")
    @Mapping(target = "latitude", expression = "java(fuelStationApi.getCoordinates().snd())")
    FuelStation toEntity(FuelStationApi fuelStationApi);
}
