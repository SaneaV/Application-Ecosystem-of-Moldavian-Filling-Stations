package md.bot.fuel.facade;

import static java.util.Objects.isNull;

import lombok.RequiredArgsConstructor;
import md.bot.fuel.domain.UserData;
import md.bot.fuel.facade.dto.UserDataDto;
import md.bot.fuel.facade.dto.UserDataDtoMapper;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.service.UserDataService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataFacadeImpl implements UserDataFacade {

  private static final String ERROR_SPECIFY_COORDINATES = "To start working with bot you have to send your geolocation";
  private static final String ERROR_COORDINATES_REASON_CODE = "COORDINATES_NOT_FOUND";
  private static final String ERROR_SPECIFY_RADIUS = "To start working with bot you have to specify search radius in kilometers (ex. 0.5 - 500 meters, 5 - 5 kilometers)";
  private static final String ERROR_RADIUS__REASON_CODE = "RADIUS_NOT_FOUND";
  private static final String ERROR_USER_NOT_FOUND = "User not found, please contact administrator";
  private static final String ERROR_USER_REASON_CODE = "USER_NOT_FOUND";

  private static final double ZERO_DOUBLE_VALUE = 0.0D;
  private static final double KILOMETERS_TO_METERS = 1000;

  private final UserDataService userDataService;
  private final UserDataDtoMapper userDataDtoMapper;

  @Override
  public UserDataDto getUserData(long userId) {
    final UserData userData = userDataService.getUserData(userId);

    if (!isNull(userData)) {
      if (userData.getLatitude() == ZERO_DOUBLE_VALUE || userData.getLongitude() == ZERO_DOUBLE_VALUE) {
        throw new EntityNotFoundException(ERROR_SPECIFY_COORDINATES, ERROR_COORDINATES_REASON_CODE);
      }
      if (userData.getRadius() == ZERO_DOUBLE_VALUE) {
        throw new EntityNotFoundException(ERROR_SPECIFY_RADIUS, ERROR_RADIUS__REASON_CODE);
      }
    } else {
      throw new EntityNotFoundException(ERROR_USER_NOT_FOUND, ERROR_USER_REASON_CODE);
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
