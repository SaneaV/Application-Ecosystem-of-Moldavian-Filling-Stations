package md.electric.api.rest.mapper;

import md.electric.api.domain.ChargingStation;
import md.electric.api.rest.dto.ChargingStationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChargingStationDtoMapper {

  @Mapping(target = "city", source = "location.city")
  @Mapping(target = "district", source = "location.district")
  @Mapping(target = "latitude", source = "location.latitude")
  @Mapping(target = "longitude", source = "location.longitude")
  ChargingStationDto toDto(ChargingStation model);
}