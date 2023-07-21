package md.bot.fuel.infrastructure.service;

import md.bot.fuel.domain.UserData;

public interface UserDataService {

    UserData getUserData(long userId);

    void save(long userId);

    void save(long userId, double radius);

    void save(long userId, double latitude, double longitude);
}
