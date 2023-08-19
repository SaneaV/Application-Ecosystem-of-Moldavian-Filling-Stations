package md.fuel.bot.infrastructure.repository;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FillingStationMapper extends PageMapper {

  String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
  DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  FillingStation toEntity(FillingStationDto fillingStationDto);

  default FuelType toEntity(List<String> supportedFuelTypes) {
    return new FuelType(supportedFuelTypes);
  }

  default String toString(ZonedDateTime updateTimestamp) {
    return FORMATTER.format(updateTimestamp);
  }
}
