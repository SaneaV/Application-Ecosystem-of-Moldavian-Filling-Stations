package md.fuel.api.infrastructure.repository;

import static java.util.Objects.isNull;

import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AnreApiMapper {

  @Mapping(target = "longitude", expression = "java(fillingStationApi.getCoordinates().fst())")
  @Mapping(target = "latitude", expression = "java(fillingStationApi.getCoordinates().snd())")
  @Mapping(target = "petrol", source = "petrol", qualifiedByName = "mapZeroOrNullPrice")
  @Mapping(target = "diesel", source = "diesel", qualifiedByName = "mapZeroOrNullPrice")
  @Mapping(target = "gas", source = "gas", qualifiedByName = "mapZeroOrNullPrice")
  FillingStation toEntity(FillingStationApi fillingStationApi);

  FuelPrice toEntity(FuelPriceApi fuelPriceApi);

  @Named("mapZeroOrNullPrice")
  default Double mapZeroOrNullPrice(Double price) {
    return isNull(price) || price.equals(0.0) ? null : price;
  }
}
