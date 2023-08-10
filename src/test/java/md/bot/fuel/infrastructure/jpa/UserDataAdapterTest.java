package md.bot.fuel.infrastructure.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import md.bot.fuel.domain.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDataAdapterTest {

  private static final long USER_ID = 10;
  private static final double USER_RADIUS = 10;
  private static final double COORDINATES = 10;

  private final UserDataRepository userDataRepository;
  private final UserDataJpaMapper mapper;
  private final UserDataAdapter userDataAdapter;

  public UserDataAdapterTest() {
    this.userDataRepository = mock(UserDataRepository.class);
    this.mapper = mock(UserDataJpaMapper.class);
    this.userDataAdapter = new UserDataAdapterImpl(userDataRepository, mapper);
  }

  @Test
  @DisplayName("Should get user data")
  void shouldGetUserData() {
    final UserData userData = new UserData();
    final UserDataJpa userDataJpa = new UserDataJpa();
    userData.setId(USER_ID);
    userData.setRadius(USER_RADIUS);
    userData.setLatitude(COORDINATES);
    userData.setLongitude(COORDINATES);

    when(mapper.toEntity(any())).thenReturn(userData);
    when(userDataRepository.findById(USER_ID)).thenReturn(userDataJpa);

    final UserData result = userDataAdapter.getUserData(USER_ID);

    assertThat(result.getId()).isEqualTo(userData.getId());
    assertThat(result.getRadius()).isEqualTo(userData.getRadius());
    assertThat(result.getLatitude()).isEqualTo(userData.getLatitude());
    assertThat(result.getLongitude()).isEqualTo(userData.getLongitude());

    verify(userDataRepository).findById(USER_ID);
    verify(mapper).toEntity(any());
  }

  @Test
  @DisplayName("Should save user data on get method")
  void shouldSaveUserDataOnGetMethod() {
    final UserData userData = new UserData();
    final UserDataJpa userDataJpa = new UserDataJpa();
    userData.setId(USER_ID);
    userData.setRadius(USER_RADIUS);
    userData.setLatitude(COORDINATES);
    userData.setLongitude(COORDINATES);

    when(mapper.toEntity(any())).thenReturn(userData);
    when(userDataRepository.findById(USER_ID)).thenReturn(null);
    when(userDataRepository.save(any())).thenReturn(userDataJpa);

    final UserData result = userDataAdapter.getUserData(USER_ID);

    assertThat(result.getId()).isEqualTo(userData.getId());
    assertThat(result.getRadius()).isEqualTo(userData.getRadius());
    assertThat(result.getLatitude()).isEqualTo(userData.getLatitude());
    assertThat(result.getLongitude()).isEqualTo(userData.getLongitude());

    verify(userDataRepository).findById(USER_ID);
    verify(userDataRepository).save(any());
    verify(mapper).toEntity(any());
  }

  @Test
  @DisplayName("Should save new user data")
  void shouldSaveNewUserData() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    final UserDataJpa userDataJpa = new UserDataJpa();

    when(mapper.toJpa(any(UserData.class))).thenReturn(userDataJpa);
    when(userDataRepository.findById(USER_ID)).thenReturn(null);

    userDataAdapter.save(userData);

    verify(userDataRepository).findById(USER_ID);
    verify(userDataRepository).save(any());
    verify(mapper).toJpa(any(UserData.class));
  }

  @Test
  @DisplayName("Should update user data")
  void shouldUpdateUserData() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    final UserDataJpa userDataJpa = new UserDataJpa();

    when(mapper.update(any(), any())).thenReturn(userDataJpa);
    when(userDataRepository.findById(USER_ID)).thenReturn(userDataJpa);

    userDataAdapter.save(userData);

    verify(userDataRepository).findById(USER_ID);
    verify(userDataRepository).save(any());
    verify(mapper).update(any(), any());
  }
}
