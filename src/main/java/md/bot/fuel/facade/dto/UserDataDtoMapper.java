package md.bot.fuel.facade.dto;

import md.bot.fuel.domain.UserData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDataDtoMapper {

  UserDataDto toDto(UserData userData);
}
