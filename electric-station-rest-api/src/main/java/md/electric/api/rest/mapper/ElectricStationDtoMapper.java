package md.electric.api.rest.mapper;

import java.util.List;
import md.electric.api.domain.ElectricStation;
import md.electric.api.rest.dto.ElectricStationDto;
import md.electric.api.rest.dto.PageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ElectricStationDtoMapper {

  @Mapping(target = "city", source = "location.city")
  @Mapping(target = "district", source = "location.district")
  @Mapping(target = "latitude", source = "location.latitude")
  @Mapping(target = "longitude", source = "location.longitude")
  ElectricStationDto toDto(ElectricStation model);

  List<ElectricStationDto> toDto(List<ElectricStation> fillingStationList);

  PageDto<ElectricStationDto> toDto(List<ElectricStation> items, int totalResults);
}