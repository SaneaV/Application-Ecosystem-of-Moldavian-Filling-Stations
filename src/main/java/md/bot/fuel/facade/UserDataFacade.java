package md.bot.fuel.facade;

import md.bot.fuel.domain.UserData;

public interface UserDataFacade {

    UserData getUserData(long userId);

    void addNewUser(long userId);

    void updateRadius(long userId, double radius);

    void updateCoordinates(long userId, double latitude, double longitude);
}
