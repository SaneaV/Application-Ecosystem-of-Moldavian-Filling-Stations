package md.fuel.bot.facade;

import static java.util.Objects.isNull;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.UserData;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.service.UserDataService;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.fuel.bot.telegram.dto.UserDataDtoMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataFacadeImpl implements UserDataFacade {

  private static final String ERROR_SPECIFY_COORDINATES = "To start working with bot you have to send your geolocation";
  private static final String ERROR_SPECIFY_RADIUS =
      "To start working with bot you have to specify search radius in kilometers (ex. 0.5 - 500 meters, 5 - 5 kilometers)";
  private static final String ERROR_USER_NOT_FOUND = "User not found, please contact administrator";

  private static final double ZERO_DOUBLE_VALUE = 0.0D;
  private static final double KILOMETERS_TO_METERS = 1000;

  private final UserDataService userDataService;
  private final UserDataDtoMapper userDataDtoMapper;

  @Override
  public UserDataDto getUserData(long userId) {
    final UserData userData = userDataService.getUserData(userId);

    if (!isNull(userData)) {
      if (userData.getLatitude() == ZERO_DOUBLE_VALUE || userData.getLongitude() == ZERO_DOUBLE_VALUE) {
        throw new EntityNotFoundException(ERROR_SPECIFY_COORDINATES);
      }
      if (userData.getRadius() == ZERO_DOUBLE_VALUE) {
        throw new EntityNotFoundException(ERROR_SPECIFY_RADIUS);
      }
    } else {
      throw new EntityNotFoundException(ERROR_USER_NOT_FOUND);
    }

    return userDataDtoMapper.toDto(userData);
  }

  @Override
  public void addNewUser(long userId) {
    userDataService.save(userId);
  }

  @Override
  public void updateRadius(long userId, double radius) {
    userDataService.save(userId, radius * KILOMETERS_TO_METERS);
  }

  @Override
  public void updateCoordinates(long userId, double latitude, double longitude) {
    userDataService.save(userId, latitude, longitude);
  }
}
