package md.bot.fuel.infrastructure.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import md.bot.fuel.domain.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDataJpaMapperTest {

  private static final double DEFAULT_DOUBLE_VALUE = 0.0;
  private static final long DEFAULT_LONG_VALUE = 0L;
  private static final Long USER_ID = 10L;
  private static final double RADIUS = 20.0;
  private static final double COORDINATES = 20.0;

  private final UserDataJpaMapper userDataJpaMapper;

  public UserDataJpaMapperTest() {
    this.userDataJpaMapper = new UserDataJpaMapperImpl();
  }

  @Test
  @DisplayName("Should map userDataJpa to userData")
  void shouldMapUserDataJpaToUserData() {
    final UserDataJpa userDataJpa = new UserDataJpa(USER_ID, RADIUS, COORDINATES, COORDINATES);

    final UserData result = userDataJpaMapper.toEntity(userDataJpa);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(RADIUS);
    assertThat(result.getLatitude()).isEqualTo(COORDINATES);
    assertThat(result.getLongitude()).isEqualTo(COORDINATES);
  }

  @Test
  @DisplayName("Should map userData to null if userDataJpa is null")
  void shouldMapUserDataJpaToNull() {
    final UserDataJpa userDataJpa = null;

    final UserData result = userDataJpaMapper.toEntity(userDataJpa);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should update userDataJpa with new data from userData")
  void shouldUpdateUserDataJpaFromUserData() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    userData.setRadius(DEFAULT_DOUBLE_VALUE);
    userData.setLatitude(DEFAULT_DOUBLE_VALUE);
    userData.setLongitude(DEFAULT_DOUBLE_VALUE);

    final UserDataJpa userDataJpa = new UserDataJpa(USER_ID, RADIUS, COORDINATES, COORDINATES);

    final UserDataJpa result = userDataJpaMapper.update(userDataJpa, userData);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLatitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLongitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
  }

  @Test
  @DisplayName("Should return null on empty userData and userDataJpa")
  void shouldReturnNullOnEmptyUserDataAndUserDataJpa() {
    final UserData userData = null;
    final UserDataJpa userDataJpa = null;

    final UserDataJpa result = userDataJpaMapper.update(userDataJpa, userData);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Shouldn't change userId in userDataJpa if null")
  void shouldNotChangeUserIdInUserDataJpaIfNull() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    userData.setRadius(DEFAULT_DOUBLE_VALUE);
    userData.setLatitude(DEFAULT_DOUBLE_VALUE);
    userData.setLongitude(DEFAULT_DOUBLE_VALUE);
    final UserDataJpa userDataJpa = null;

    final UserDataJpa result = userDataJpaMapper.update(userDataJpa, userData);

    assertThat(result.getId()).isEqualTo(DEFAULT_LONG_VALUE);
    assertThat(result.getRadius()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLatitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLongitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
  }

  @Test
  @DisplayName("Shouldn't change radius and coordinates in userDataJpa if userData is null")
  void shouldNotChangeRadiusAndCoordinatesIfUserDataIsNull() {
    final UserData userData = null;
    final UserDataJpa userDataJpa = new UserDataJpa(USER_ID, RADIUS, COORDINATES, COORDINATES);

    final UserDataJpa result = userDataJpaMapper.update(userDataJpa, userData);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLatitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLongitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
  }

  @Test
  @DisplayName("Should map to userDataJpa from userData")
  void shouldMapToUserDataJpaFromUserData() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    userData.setRadius(RADIUS);
    userData.setLatitude(COORDINATES);
    userData.setLongitude(COORDINATES);

    final UserDataJpa result = userDataJpaMapper.toJpa(userData);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(RADIUS);
    assertThat(result.getLatitude()).isEqualTo(COORDINATES);
    assertThat(result.getLongitude()).isEqualTo(COORDINATES);
  }

  @Test
  @DisplayName("Should map userDataJpa to null if userData is null")
  void shouldMapUserDataJpaToNullIfUserDataIsNull() {
    final UserData userData = null;

    final UserDataJpa result = userDataJpaMapper.toJpa(userData);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should map userId to empty userDataJpa")
  void shouldMapUserIdToEmptyUserDataJpa() {
    final UserDataJpa result = userDataJpaMapper.toJpa(USER_ID);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLatitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLongitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
  }

  @Test
  @DisplayName("Should return null userDataJpa if userId is null")
  void shouldReturnNullUserDataJpaIfUserIdIsNull() {
    final Long userId = null;

    final UserDataJpa result = userDataJpaMapper.toJpa(userId);

    assertThat(result).isNull();
  }
}