package md.fuel.bot.facade;

import md.fuel.bot.telegram.dto.UserDataDto;

public interface UserDataFacade {

  UserDataDto getUserData(long userId);

  void addNewUser(long userId);

  void updateRadius(long userId, double radius);

  void updateCoordinates(long userId, double latitude, double longitude);

  void updateLanguage(long userId, String language);

  String getLanguage(long userId);
}
