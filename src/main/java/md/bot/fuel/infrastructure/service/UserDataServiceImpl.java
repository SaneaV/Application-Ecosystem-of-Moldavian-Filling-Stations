package md.bot.fuel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import md.bot.fuel.domain.UserData;
import md.bot.fuel.infrastructure.jpa.UserDataAdapter;
import md.bot.fuel.infrastructure.mapper.UserDataMapper;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

    private final UserDataAdapter userDataAdapter;
    private final UserDataMapper mapper;

    @Override
    public UserData getUserData(long userId) {
        return userDataAdapter.getUserData(userId);
    }

    @Override
    public void save(long userId) {
        final UserData userDataFromDb = userDataAdapter.getUserData(userId);
        if (isNull(userDataFromDb)) {
            final UserData userData = mapper.toEntity(userId);
            userDataAdapter.save(userData);
        }
    }

    @Override
    public void save(long userId, double radius) {
        final UserData userData = userDataAdapter.getUserData(userId);
        final UserData updatedUserData = mapper.update(userData, radius);
        userDataAdapter.save(updatedUserData);
    }

    @Override
    public void save(long userId, double latitude, double longitude) {
        final UserData userData = userDataAdapter.getUserData(userId);
        final UserData updatedUserData = mapper.update(userData, latitude, longitude);
        userDataAdapter.save(updatedUserData);
    }
}
