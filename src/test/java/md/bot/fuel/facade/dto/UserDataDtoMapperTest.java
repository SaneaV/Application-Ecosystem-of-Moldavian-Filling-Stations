package md.bot.fuel.facade.dto;

import md.bot.fuel.domain.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDataDtoMapperTest {

    private static final long USER_ID = 10;
    private static final long LATITUDE = 20;
    private static final long LONGITUDE = 30;
    private static final long RADIUS = 40;

    private final UserDataDtoMapper userDataDtoMapper;

    public UserDataDtoMapperTest() {
        this.userDataDtoMapper = new UserDataDtoMapperImpl();
    }

    @Test
    @DisplayName("Should map userData to userDataDto")
    void shouldMapUserDataToUserDataDto() {
        final UserData userData = new UserData();
        userData.setId(USER_ID);
        userData.setRadius(RADIUS);
        userData.setLatitude(LATITUDE);
        userData.setLongitude(LONGITUDE);

        final UserDataDto result = userDataDtoMapper.toDto(userData);

        assertThat(result.getId()).isEqualTo(userData.getId());
        assertThat(result.getRadius()).isEqualTo(userData.getRadius());
        assertThat(result.getLatitude()).isEqualTo(userData.getLatitude());
        assertThat(result.getLongitude()).isEqualTo(userData.getLongitude());
    }

    @Test
    @DisplayName("Should map userDataDto to null on null userData")
    void shouldMapUserDataDtoToNullOnNullUserData() {
        final UserData userData = null;

        final UserDataDto result = userDataDtoMapper.toDto(userData);

        assertThat(result).isNull();
    }
}
