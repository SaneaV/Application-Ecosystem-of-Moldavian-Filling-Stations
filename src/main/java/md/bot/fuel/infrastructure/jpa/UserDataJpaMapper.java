package md.bot.fuel.infrastructure.jpa;

import md.bot.fuel.domain.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDataJpaMapper {

  UserData toEntity(UserDataJpa userDataJpa);

  @Mapping(target = "id", source = "userDataJpa.id")
  @Mapping(target = "radius", source = "userData.radius")
  @Mapping(target = "latitude", source = "userData.latitude")
  @Mapping(target = "longitude", source = "userData.longitude")
  UserDataJpa update(UserDataJpa userDataJpa, UserData userData);

  UserDataJpa toJpa(UserData userData);

  UserDataJpa toJpa(Long id);
}
