package md.fuel.bot.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import md.fuel.bot.domain.UserData;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.service.UserDataService;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.fuel.bot.telegram.dto.UserDataDtoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDataFacadeImplTest {

  private static final String ERROR_SPECIFY_COORDINATES = "To start working with bot you have to send your geolocation";
  private static final String ERROR_SPECIFY_RADIUS =
      "To start working with bot you have to specify search radius in kilometers (ex. 0.5 - 500 meters, 5 - 5 kilometers)";
  private static final String ERROR_USER_NOT_FOUND = "User not found, please contact administrator";

  private static final long USER_ID = 10;
  private static final long LATITUDE = 20;
  private static final long LONGITUDE = 30;
  private static final long RADIUS = 40;

  private final UserDataService userDataService;
  private final UserDataDtoMapper userDataDtoMapper;
  private final UserDataFacade userDataFacade;

  public UserDataFacadeImplTest() {
    this.userDataService = mock(UserDataService.class);
    this.userDataDtoMapper = mock(UserDataDtoMapper.class);
    this.userDataFacade = new UserDataFacadeImpl(userDataService, userDataDtoMapper);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when getUserData from service is null")
  void shouldThrowEntityNotFoundExceptionOnNullUserData() {
    when(userDataService.getUserData(USER_ID)).thenReturn(null);

    assertThatThrownBy(() -> userDataFacade.getUserData(USER_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_USER_NOT_FOUND);

    verify(userDataService).getUserData(anyLong());
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when UserData latitude is null")
  void shouldThrowEntityNotFoundExceptionOnNullLatitude() {
    final UserData userData = new UserData();
    when(userDataService.getUserData(USER_ID)).thenReturn(userData);

    assertThatThrownBy(() -> userDataFacade.getUserData(USER_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_SPECIFY_COORDINATES);

    verify(userDataService).getUserData(anyLong());
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when UserData longitude is null")
  void shouldThrowEntityNotFoundExceptionOnNullLongitude() {
    final UserData userData = new UserData();
    userData.setLatitude(LATITUDE);
    when(userDataService.getUserData(USER_ID)).thenReturn(userData);

    assertThatThrownBy(() -> userDataFacade.getUserData(USER_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_SPECIFY_COORDINATES);

    verify(userDataService).getUserData(anyLong());
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException when UserData radius is null")
  void shouldThrowEntityNotFoundExceptionOnNullRadius() {
    final UserData userData = new UserData();
    userData.setLatitude(LATITUDE);
    userData.setLongitude(LONGITUDE);
    when(userDataService.getUserData(USER_ID)).thenReturn(userData);

    assertThatThrownBy(() -> userDataFacade.getUserData(USER_ID))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_SPECIFY_RADIUS);

    verify(userDataService).getUserData(anyLong());
  }

  @Test
  @DisplayName("Should return user data")
  void shouldReturnUserData() {
    final UserData userData = new UserData();
    final UserDataDto userDataDto = new UserDataDto(USER_ID, RADIUS, LATITUDE, LONGITUDE);
    userData.setId(USER_ID);
    userData.setLatitude(LATITUDE);
    userData.setLongitude(LONGITUDE);
    userData.setRadius(RADIUS);
    when(userDataService.getUserData(USER_ID)).thenReturn(userData);
    when(userDataDtoMapper.toDto(any())).thenReturn(userDataDto);

    final UserDataDto result = userDataFacade.getUserData(USER_ID);

    verify(userDataService).getUserData(anyLong());
    verify(userDataDtoMapper).toDto(any());
    assertThat(result.id()).isEqualTo(userData.getId());
    assertThat(result.latitude()).isEqualTo(userData.getLatitude());
    assertThat(result.longitude()).isEqualTo(userData.getLongitude());
    assertThat(result.radius()).isEqualTo(userData.getRadius());
  }

  @Test
  @DisplayName("Should add new userData with userId")
  void shouldSaveUserDataWithUserId() {
    userDataFacade.addNewUser(USER_ID);
    verify(userDataService).save(anyLong());
  }

  @Test
  @DisplayName("Should update user radius")
  void shouldUpdateUserRadius() {
    userDataFacade.updateRadius(USER_ID, RADIUS);
    verify(userDataService).save(anyLong(), anyDouble());
  }

  @Test
  @DisplayName("Should update user radius")
  void shouldUpdateUserCoordinates() {
    userDataFacade.updateCoordinates(USER_ID, LATITUDE, LONGITUDE);
    verify(userDataService).save(anyLong(), anyDouble(), anyDouble());
  }
}
