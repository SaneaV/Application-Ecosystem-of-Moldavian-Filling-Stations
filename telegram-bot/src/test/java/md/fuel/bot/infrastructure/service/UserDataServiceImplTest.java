package md.fuel.bot.infrastructure.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import md.fuel.bot.domain.UserData;
import md.fuel.bot.infrastructure.jpa.UserDataAdapter;
import md.fuel.bot.infrastructure.mapper.UserDataMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDataServiceImplTest {

  private static final long USER_ID = 1;

  private final UserDataService userDataService;
  private final UserDataAdapter userDataAdapter;
  private final UserDataMapper userDataMapper;

  public UserDataServiceImplTest() {
    this.userDataAdapter = mock(UserDataAdapter.class);
    this.userDataMapper = mock(UserDataMapper.class);
    this.userDataService = new UserDataServiceImpl(userDataAdapter, userDataMapper);
  }

  @Test
  @DisplayName("Should return user data")
  void shouldReturnUserData() {
    final UserData userData = new UserData();
    when(userDataAdapter.getUserData(USER_ID)).thenReturn(userData);

    final UserData result = userDataService.getUserData(USER_ID);

    assertThat(result).isEqualTo(userData);
    verify(userDataAdapter).getUserData(USER_ID);
  }

  @Test
  @DisplayName("Should save user data if not found in database")
  void shouldSaveUserDataIfNotFoundInDatabase() {
    final UserData newUserData = new UserData();
    when(userDataAdapter.getUserData(USER_ID)).thenReturn(null);
    when(userDataMapper.toEntity(USER_ID)).thenReturn(newUserData);

    userDataService.save(USER_ID);

    verify(userDataAdapter).getUserData(USER_ID);
    verify(userDataMapper).toEntity(USER_ID);
    verify(userDataAdapter).save(newUserData);
  }

  @Test
  @DisplayName("Should return existing user on save method")
  void shouldReturnExistingUserOnSaveMethod() {
    final UserData userData = new UserData();
    when(userDataAdapter.getUserData(USER_ID)).thenReturn(userData);

    userDataService.save(USER_ID);

    verify(userDataAdapter).getUserData(USER_ID);
    verifyNoInteractions(userDataMapper);
  }

  @Test
  @DisplayName("Should save new user radius")
  void shouldSaveNewUserRadius() {
    final double radius = 500;
    final UserData userData = new UserData();
    when(userDataAdapter.getUserData(USER_ID)).thenReturn(userData);

    userDataService.save(USER_ID, radius);

    verify(userDataAdapter).getUserData(USER_ID);
    verify(userDataMapper).update(userData, radius);
  }

  @Test
  @DisplayName("Should save new user coordinates")
  void shouldSaveNewUserCoordinates() {
    final double coordinates = 500;
    final UserData userData = new UserData();
    when(userDataAdapter.getUserData(USER_ID)).thenReturn(userData);

    userDataService.save(USER_ID, coordinates, coordinates);

    verify(userDataAdapter).getUserData(USER_ID);
    verify(userDataMapper).update(userData, coordinates, coordinates);
  }
}
