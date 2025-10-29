package md.fuel.bot.infrastructure.service;

import md.fuel.bot.domain.StationType;
import md.fuel.bot.domain.UserData;

public interface UserDataService {

  UserData getUserData(long userId);

  void save(long userId);

  void save(long userId, double radius);

  void save(long userId, double latitude, double longitude);

  void save(long userId, String language);

  void save(long userId, StationType stationType);
}
