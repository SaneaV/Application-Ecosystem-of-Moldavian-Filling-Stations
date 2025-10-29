package md.fuel.bot.infrastructure.mapper;

import md.fuel.bot.domain.StationType;
import md.fuel.bot.domain.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDataMapper {

  @Mapping(target = "id", source = "userId")
  @Mapping(target = "language", ignore = true)
  @Mapping(target = "radius", ignore = true)
  @Mapping(target = "latitude", ignore = true)
  @Mapping(target = "longitude", ignore = true)
  @Mapping(target = "stationType", ignore = true)
  UserData toEntity(Long userId);

  @Mapping(target = "radius", source = "radius")
  UserData update(UserData userData, double radius);

  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "longitude", source = "longitude")
  UserData update(UserData userData, double latitude, double longitude);

  @Mapping(target = "language", source = "language")
  UserData update(UserData userData, String language);

  @Mapping(target = "stationType", source = "stationType")
  UserData update(UserData userData, StationType stationType);
}