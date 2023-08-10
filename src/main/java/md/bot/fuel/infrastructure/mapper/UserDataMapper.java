package md.bot.fuel.infrastructure.mapper;

import md.bot.fuel.domain.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDataMapper {

  @Mapping(target = "id", source = "userId")
  UserData toEntity(Long userId);

  @Mapping(target = "radius", source = "radius")
  UserData update(UserData userData, double radius);

  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "longitude", source = "longitude")
  UserData update(UserData userData, double latitude, double longitude);
}
