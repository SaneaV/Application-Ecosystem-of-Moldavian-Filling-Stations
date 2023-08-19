package md.fuel.bot.telegram.dto;

import md.fuel.bot.domain.UserData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDataDtoMapper {

  UserDataDto toDto(UserData userData);
}
