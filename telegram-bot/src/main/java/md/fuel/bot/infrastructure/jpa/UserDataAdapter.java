package md.fuel.bot.infrastructure.jpa;

import md.fuel.bot.domain.UserData;

public interface UserDataAdapter {

  UserData getUserData(long userId);

  void delete(long userId);

  void save(UserData userData);
}