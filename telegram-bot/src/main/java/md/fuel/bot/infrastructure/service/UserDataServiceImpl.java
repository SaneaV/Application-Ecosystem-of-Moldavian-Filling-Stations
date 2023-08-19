package md.fuel.bot.infrastructure.service;

import static java.util.Objects.isNull;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.UserData;
import md.fuel.bot.infrastructure.jpa.UserDataAdapter;
import md.fuel.bot.infrastructure.mapper.UserDataMapper;
import org.springframework.stereotype.Service;

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
