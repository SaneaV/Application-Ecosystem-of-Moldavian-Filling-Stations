package md.bot.fuel.infrastructure.api;

import md.bot.fuel.domain.FuelStation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface AnreApiMapper {

    @Mapping(target = "longitude", expression = "java(fuelStationApi.getCoordinates().fst())")
    @Mapping(target = "latitude", expression = "java(fuelStationApi.getCoordinates().snd())")
    @Mapping(target = "petrol", source = "petrol", qualifiedByName = "mapZeroOrNullPrice")
    @Mapping(target = "diesel", source = "diesel", qualifiedByName = "mapZeroOrNullPrice")
    @Mapping(target = "gas", source = "gas", qualifiedByName = "mapZeroOrNullPrice")
    FuelStation toEntity(FuelStationApi fuelStationApi);

    @Named("mapZeroOrNullPrice")
    default Double mapZeroOrNullPrice(Double price) {
        return isNull(price) || price.equals(0.0) ? null : price;
    }
}
