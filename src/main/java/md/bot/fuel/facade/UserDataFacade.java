package md.bot.fuel.facade;

import md.bot.fuel.facade.dto.UserDataDto;

public interface UserDataFacade {

    UserDataDto getUserData(long userId);

    void addNewUser(long userId);

    void updateRadius(long userId, double radius);

    void updateCoordinates(long userId, double latitude, double longitude);
}
