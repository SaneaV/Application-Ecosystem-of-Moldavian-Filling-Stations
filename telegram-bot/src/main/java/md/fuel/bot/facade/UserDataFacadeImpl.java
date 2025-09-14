package md.fuel.bot.facade;

import static java.util.Objects.isNull;
import static md.fuel.bot.telegram.exception.ErrorCode.ERROR_CODE_SPECIFY_LANGUAGE;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    if (!isNull(userData)) {
      if (isEmpty(userData.getLanguage())) {
        log.info("The user ({}) did not specify bot language", userId);
        final String exceptionMessage = translatorService.translate("en", ERROR_LANGUAGE_NOT_SPECIFIED);

        throw new EntityNotFoundException(exceptionMessage, ERROR_CODE_SPECIFY_LANGUAGE);
      }

      if (userData.getLatitude() == ZERO_DOUBLE_VALUE || userData.getLongitude() == ZERO_DOUBLE_VALUE) {
        log.info("The user ({}) did not specify the latitude and longitude", userId);
        final String exceptionMessage = translatorService.translate(userData.getLanguage(), ERROR_SPECIFY_COORDINATES);

        throw new EntityNotFoundException(exceptionMessage);
      }

      if (userData.getRadius() == ZERO_DOUBLE_VALUE) {
        log.info("The user ({}) did not specify the search radius", userId);
        final String exceptionMessage = translatorService.translate(userData.getLanguage(), ERROR_SPECIFY_RADIUS);

        throw new EntityNotFoundException(exceptionMessage);
      }

    } else {
      log.error("User with id {} not found", userId);
      final String exceptionMessage = translatorService.translate("en", ERROR_USER_NOT_FOUND);

      throw new EntityNotFoundException(exceptionMessage);
    }

    return userDataDtoMapper.toDto(userData);
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
  public String getLanguage(long userId) {
    log.info("Get language for user = {}", userId);

    final String language = userDataService.getUserData(userId).getLanguage();

    return isEmpty(language)? defaultLanguage :  language;
  }
}
