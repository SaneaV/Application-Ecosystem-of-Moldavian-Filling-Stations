package md.bot.fuel.infrastructure.jpa;

import md.bot.fuel.domain.UserData;

public interface UserDataAdapter {

  UserData getUserData(long userId);

  void save(UserData userData);
}