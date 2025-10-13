package md.fuel.bot.infrastructure.repository;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import md.fuel.bot.domain.FillingStation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FillingStationMapperTest {

  private final FillingStationMapperImpl mapper = new FillingStationMapperImpl();

  @Test
  @DisplayName("Should return null on null FillingStationDto")
  void shouldReturnNullOnNullFillingStationDto() {
    final FillingStation result = mapper.toEntity((FillingStationDto) null);
    assertThat(result).isNull();
  }

//  @Test
//  @DisplayName("Should return 0.0d longitude and latitude on nulls from FillingStationDto")
//  void shouldReturnDefaultDoublesOnNullsFromFillingStationDto() {
//    final FillingStationDto fillingStationDto = new FillingStationDto(EMPTY, null, null, null, null, null);
//    final FillingStation result = mapper.toEntity(fillingStationDto);
//    assertThat(result.getLatitude()).isEqualTo(0.0d);
//    assertThat(result.getLongitude()).isEqualTo(0.0d);
//  }
}
