package md.fuel.bot.infrastructure.jpa;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import md.fuel.bot.domain.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDataJpaMapper {

  UserData toEntity(UserDataJpa userDataJpa);

  @Mapping(target = "id", source = "userDataJpa.id")
  @Mapping(target = "language", source = "userData.language")
  @Mapping(target = "radius", source = "userData.radius")
  @Mapping(target = "latitude", source = "userData.latitude")
  @Mapping(target = "longitude", source = "userData.longitude")
  UserDataJpa update(UserDataJpa userDataJpa, UserData userData);

  UserDataJpa toJpa(UserData userData);

  @Mapping(target = "radius", ignore = true)
  @Mapping(target = "language", constant = EMPTY)
  @Mapping(target = "latitude", ignore = true)
  @Mapping(target = "longitude", ignore = true)
  UserDataJpa toJpa(Long id);
}
