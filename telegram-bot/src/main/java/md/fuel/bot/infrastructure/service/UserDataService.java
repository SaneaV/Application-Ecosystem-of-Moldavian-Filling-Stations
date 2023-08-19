package md.fuel.bot.infrastructure.service;

import md.fuel.bot.domain.UserData;

public interface UserDataService {

  UserData getUserData(long userId);

  void save(long userId);

  void save(long userId, double radius);

  void save(long userId, double latitude, double longitude);
}
