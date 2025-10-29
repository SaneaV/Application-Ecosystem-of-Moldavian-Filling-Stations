package md.fuel.bot.facade;

import md.fuel.bot.domain.StationType;
import md.fuel.bot.telegram.dto.UserDataDto;

public interface UserDataFacade {

  UserDataDto getUserData(long userId);

  void addNewUser(long userId);

  void updateRadius(long userId, double radius);

  void updateCoordinates(long userId, double latitude, double longitude);

  void updateLanguage(long userId, String language);

  void updateStationType(long userId, StationType stationType);

  String getLanguage(long userId);

  StationType getStationType(long userId);
}
