package md.fuel.bot.facade;

import static java.util.Objects.isNull;
import static md.fuel.bot.telegram.exception.ErrorCode.ERROR_CODE_SPECIFY_LANGUAGE;
import static md.fuel.bot.telegram.exception.ErrorCode.ERROR_CODE_SPECIFY_STATION_TYPE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.StationType;
import md.fuel.bot.domain.UserData;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.fuel.bot.infrastructure.service.UserDataService;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.fuel.bot.telegram.dto.UserDataDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataFacadeImpl implements UserDataFacade {

  private static final String ERROR_SPECIFY_COORDINATES = "error.specify-coordinates.message";
  private static final String ERROR_SPECIFY_RADIUS = "error.specify-radius.message";
  private static final String ERROR_USER_NOT_FOUND = "error.user-not-found.message";
  private static final String ERROR_LANGUAGE_NOT_SPECIFIED = "error.specify-language.message";
  private static final String ERROR_STATION_TYPE_NOT_SPECIFIED = "error.specify-station-type.message";

  @Value("${telegram.bot.translation.default-language}")
  private String defaultLanguage;

  private static final double ZERO_DOUBLE_VALUE = 0.0D;
  private static final double KILOMETERS_TO_METERS = 1000;

  private final UserDataService userDataService;
  private final UserDataDtoMapper userDataDtoMapper;
  private final TranslatorService translatorService;

  @Override
  public UserDataDto getUserData(long userId) {
    log.info("Fetch user data for user id = {}", userId);
    final UserData userData = userDataService.getUserData(userId);

    if (isNull(userData)) {
      log.error("User with id {} not found", userId);
      throw new EntityNotFoundException(
          translatorService.translate("en", ERROR_USER_NOT_FOUND));
    }

    validateUserData(userId, userData);

    return userDataDtoMapper.toDto(userData);
  }

  private void validateUserData(long userId, UserData userData) {
    if (isEmpty(userData.getLanguage())) {
      log.info("The user ({}) did not specify bot language", userId);
      throw new EntityNotFoundException(
          translatorService.translate("en", ERROR_LANGUAGE_NOT_SPECIFIED),
          ERROR_CODE_SPECIFY_LANGUAGE);
    }

    final String language = userData.getLanguage();

    if (isNull(userData.getStationType())) {
      log.info("The user ({}) did not specify station type", userId);
      throw new EntityNotFoundException(
          translatorService.translate(language, ERROR_STATION_TYPE_NOT_SPECIFIED),
          ERROR_CODE_SPECIFY_STATION_TYPE);
    }

    if (userData.getLatitude() == ZERO_DOUBLE_VALUE || userData.getLongitude() == ZERO_DOUBLE_VALUE) {
      log.info("The user ({}) did not specify the latitude and longitude", userId);
      throw new EntityNotFoundException(
          translatorService.translate(language, ERROR_SPECIFY_COORDINATES));
    }

    if (userData.getRadius() == ZERO_DOUBLE_VALUE) {
      log.info("The user ({}) did not specify the search radius", userId);
      throw new EntityNotFoundException(
          translatorService.translate(language, ERROR_SPECIFY_RADIUS));
    }
  }

  @Override
  public void addNewUser(long userId) {
    log.info("Add new user with id = {}", userId);

    userDataService.save(userId);
  }

  @Override
  public void updateRadius(long userId, double radius) {
    log.info("Update radius for user = {}, in kilometers = {}", userId, radius);

    userDataService.save(userId, radius * KILOMETERS_TO_METERS);
  }

  @Override
  public void updateCoordinates(long userId, double latitude, double longitude) {
    log.info("Update coordinates for user = {}, latitude = {}, longitude = {}", userId, latitude, longitude);

    userDataService.save(userId, latitude, longitude);
  }

  @Override
  public void updateLanguage(long userId, String language) {
    log.info("Update language for user = {}, language = {}", userId, language);

    userDataService.save(userId, language);
  }

  @Override
  public void updateStationType(long userId, StationType stationType) {
    log.info("Update station type for user = {}, stationType = {}", userId, stationType);

    userDataService.save(userId, stationType);
  }

  @Override
  public String getLanguage(long userId) {
    log.info("Get language for user = {}", userId);

    final String language = userDataService.getUserData(userId).getLanguage();

    return isEmpty(language)? defaultLanguage :  language;
  }

  @Override
  public StationType getStationType(long userId) {
    log.info("Get station type for user = {}", userId);

    final UserData userData = userDataService.getUserData(userId);
    return isNull(userData) ? null : userData.getStationType();
  }
}
