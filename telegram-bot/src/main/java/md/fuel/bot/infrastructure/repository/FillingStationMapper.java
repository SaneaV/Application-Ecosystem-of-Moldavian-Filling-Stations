package md.fuel.bot.infrastructure.repository;

import static java.util.Arrays.stream;
import static md.fuel.bot.infrastructure.repository.MapperConstants.EXCEPTION_PRICE_MAPPING_MESSAGE;
import static md.fuel.bot.infrastructure.repository.MapperConstants.FORMATTER;
import static md.fuel.bot.infrastructure.repository.MapperConstants.LATITUDE;
import static md.fuel.bot.infrastructure.repository.MapperConstants.LONGITUDE;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.infrastructure.exception.model.ExecutionException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FillingStationMapper extends PageMapper {

  @Mapping(target = "prices", source = "fillingStationDto", qualifiedByName = "toPriceMap")
  FillingStation toEntity(FillingStationDto fillingStationDto);

  default FuelType toEntity(List<String> supportedFuelTypes) {
    return new FuelType(supportedFuelTypes);
  }

  default String toString(ZonedDateTime updateTimestamp) {
    return FORMATTER.format(updateTimestamp);
  }

  @Named("toPriceMap")
  default Map<String, Double> toPriceMap(FillingStationDto fillingStationDto) {
    final Map<String, Double> priceMap = new LinkedHashMap<>();
    final Field[] allFields = FillingStationDto.class.getDeclaredFields();

    stream(allFields)
        .filter(field -> field.getType().equals(Double.class) &&
            !field.getName().equalsIgnoreCase(LATITUDE) &&
            !field.getName().equalsIgnoreCase(LONGITUDE))
        .forEach(field -> {
          try {
            field.setAccessible(true);
            priceMap.put(capitalize(field.getName()), (Double) field.get(fillingStationDto));
            field.setAccessible(false);
          } catch (IllegalAccessException e) {
            throw new ExecutionException(EXCEPTION_PRICE_MAPPING_MESSAGE);
          }
        });

    return priceMap;
  }
}
