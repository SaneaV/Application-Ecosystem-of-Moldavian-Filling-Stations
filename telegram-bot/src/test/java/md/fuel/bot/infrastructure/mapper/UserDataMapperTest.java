package md.fuel.bot.infrastructure.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import md.fuel.bot.domain.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserDataMapperTest {

  private static final double DEFAULT_DOUBLE_VALUE = 0.0;
  private static final Long USER_ID = 10L;
  private static final double RADIUS = 20.0;
  private static final double COORDINATES = 20.0;

  private final UserDataMapper userDataMapper;

  public UserDataMapperTest() {
    this.userDataMapper = new UserDataMapperImpl();
  }

  @Test
  @DisplayName("Should map user data with userId only")
  void shouldMapUserData() {
    final UserData result = userDataMapper.toEntity(USER_ID);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLatitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLongitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
  }

  @Test
  @DisplayName("Should map user data to null if userId is null")
  void shouldMapUserDataToNull() {
    final Long userId = null;

    final UserData result = userDataMapper.toEntity(userId);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should map user data with new radius")
  void shouldMapUserDataWithNewRadius() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    userData.setRadius(DEFAULT_DOUBLE_VALUE);
    userData.setLatitude(DEFAULT_DOUBLE_VALUE);
    userData.setLongitude(DEFAULT_DOUBLE_VALUE);

    final UserData result = userDataMapper.update(userData, RADIUS);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(RADIUS);
    assertThat(result.getLatitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLongitude()).isEqualTo(DEFAULT_DOUBLE_VALUE);
  }

  @Test
  @DisplayName("Should map user data to null if userData parameter is null on radius update")
  void shouldMapUserDataToNullOnNullUserDataParameterOnRadiusUpdate() {
    final UserData userData = null;
    final UserData result = userDataMapper.update(userData, RADIUS);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should map user data with new coordinates")
  void shouldMapUserDataWithNewCoordinates() {
    final UserData userData = new UserData();
    userData.setId(USER_ID);
    userData.setRadius(DEFAULT_DOUBLE_VALUE);
    userData.setLatitude(DEFAULT_DOUBLE_VALUE);
    userData.setLongitude(DEFAULT_DOUBLE_VALUE);

    final UserData result = userDataMapper.update(userData, COORDINATES, COORDINATES);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getRadius()).isEqualTo(DEFAULT_DOUBLE_VALUE);
    assertThat(result.getLatitude()).isEqualTo(COORDINATES);
    assertThat(result.getLongitude()).isEqualTo(COORDINATES);
  }

  @Test
  @DisplayName("Should map user data to null if userData parameter is null on coordinates update")
  void shouldMapUserDataToNullOnNullUserDataParameterOnCoordinatesUpdate() {
    final UserData userData = null;
    final UserData result = userDataMapper.update(userData, COORDINATES, COORDINATES);

    assertThat(result).isNull();
  }
}