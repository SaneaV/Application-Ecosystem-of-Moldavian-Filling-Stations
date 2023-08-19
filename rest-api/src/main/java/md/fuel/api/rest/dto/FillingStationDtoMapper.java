package md.fuel.api.rest.dto;

import md.fuel.api.domain.FillingStation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FillingStationDtoMapper {

  FillingStationDto toDto(FillingStation fillingStation);
}
