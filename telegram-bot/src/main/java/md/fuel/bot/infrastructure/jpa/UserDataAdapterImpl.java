package md.fuel.bot.infrastructure.jpa;

import static java.util.Objects.isNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.UserData;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataAdapterImpl implements UserDataAdapter {

  private final UserDataRepository userDataRepository;
  private final UserDataJpaMapper mapper;

  @Override
  public UserData getUserData(long userId) {
    log.info("Fetching user data for userId = {}", userId);
    final UserDataJpa userDataJpa = getOrCreateUser(userId);
    return mapper.toEntity(userDataJpa);
  }

  @Override
  public void delete(long userId) {
    log.info("Delete user with userId = {}", userId);
    userDataRepository.deleteById(String.valueOf(userId));
  }

  @Override
  public void save(UserData userData) {
    final UserDataJpa userDataJpa = userDataRepository.findById(userData.getId());

    if (isNull(userDataJpa)) {
      log.info("Save user with userId = {}", userData.getId());
      final UserDataJpa newUser = mapper.toJpa(userData);
      userDataRepository.save(newUser);
    } else {
      log.info("Update user with userId = {}", userData.getId());
      final UserDataJpa updatedUser = mapper.update(userDataJpa, userData);
      userDataRepository.save(updatedUser);
    }
  }

  private UserDataJpa getOrCreateUser(long userId) {
    final UserDataJpa userDataJpa = userDataRepository.findById(userId);
    return isNull(userDataJpa) ? userDataRepository.save(mapper.toJpa(userId)) : userDataJpa;
  }
}
