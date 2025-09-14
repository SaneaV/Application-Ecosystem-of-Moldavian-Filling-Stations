package md.fuel.bot.infrastructure.service;

import static java.util.Objects.isNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.UserData;
import md.fuel.bot.infrastructure.jpa.UserDataAdapter;
import md.fuel.bot.infrastructure.mapper.UserDataMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDataServiceImpl implements UserDataService {

  private final UserDataAdapter userDataAdapter;
  private final UserDataMapper mapper;

  @Override
  public UserData getUserData(long userId) {
    log.info("Fetching user data for userId = {}", userId);
    return userDataAdapter.getUserData(userId);
  }

  @Override
  public void save(long userId) {
    log.info("Save user with userId = {}", userId);
    final UserData userDataFromDb = userDataAdapter.getUserData(userId);
    if (isNull(userDataFromDb)) {
      final UserData userData = mapper.toEntity(userId);
      userDataAdapter.save(userData);
    }
  }

  @Override
  public void save(long userId, double radius) {
    log.info("Update radius to {} meters for userId = {}", radius, userId);
    final UserData userData = userDataAdapter.getUserData(userId);
    final UserData updatedUserData = mapper.update(userData, radius);
    userDataAdapter.save(updatedUserData);
  }

  @Override
  public void save(long userId, double latitude, double longitude) {
    log.info("Update latitude to {} and longitude to {} for userId = {}", latitude, longitude, userId);
    final UserData userData = userDataAdapter.getUserData(userId);
    final UserData updatedUserData = mapper.update(userData, latitude, longitude);
    userDataAdapter.save(updatedUserData);
  }

  @Override
  public void save(long userId, String language) {
    log.info("Update language for user = {}, language = {}", userId, language);
    final UserData userData = userDataAdapter.getUserData(userId);
    final UserData updatedUserData = mapper.update(userData, language);
    userDataAdapter.save(updatedUserData);
  }
}
