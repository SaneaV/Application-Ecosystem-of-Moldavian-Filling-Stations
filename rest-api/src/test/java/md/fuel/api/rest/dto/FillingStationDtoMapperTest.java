package md.fuel.api.rest.dto;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import md.fuel.api.domain.FillingStation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FillingStationDtoMapperTest {

  private static final String FILLING_STATION_NAME = "Filling Station";
  private static final double PETROL_PRICE = 10;
  private static final double DIESEL_PRICE = 20;
  private static final double GAS_PRICE = 30;
  private static final double LATITUDE = 10;
  private static final double LONGITUDE = 10;

  private final FillingStationDtoMapper FillingStationDtoMapper;

  public FillingStationDtoMapperTest() {
    this.FillingStationDtoMapper = new FillingStationDtoMapperImpl();
  }

  @Test
  @DisplayName("Should map FillingStation to FillingStationDto")
  void shouldMapFillingStationToFillingStationDto() {
    final FillingStation fillingStation = new FillingStation(FILLING_STATION_NAME, PETROL_PRICE, DIESEL_PRICE, GAS_PRICE,
        LATITUDE, LONGITUDE);

    final FillingStationDto result = FillingStationDtoMapper.toDto(fillingStation);

    assertThat(result.name()).isEqualTo(fillingStation.name());
    assertThat(result.petrol()).isEqualTo(fillingStation.petrol());
    assertThat(result.diesel()).isEqualTo(fillingStation.diesel());
    assertThat(result.gas()).isEqualTo(fillingStation.gas());
    assertThat(result.latitude()).isEqualTo(fillingStation.latitude());
    assertThat(result.longitude()).isEqualTo(fillingStation.longitude());
  }

  @Test
  @DisplayName("Should map list of FillingStation to list ofFillingStationDto")
  void shouldMapListOfFillingStationToListOfFillingStationDto() {
    final FillingStation fillingStation = new FillingStation(FILLING_STATION_NAME, PETROL_PRICE, DIESEL_PRICE, GAS_PRICE,
        LATITUDE, LONGITUDE);

    final List<FillingStationDto> result = FillingStationDtoMapper.toDto(singletonList(fillingStation));

    assertThat(result).hasSize(1);
    final FillingStationDto fillingStationDto = result.get(0);
    assertThat(fillingStationDto.name()).isEqualTo(fillingStation.name());
    assertThat(fillingStationDto.petrol()).isEqualTo(fillingStation.petrol());
    assertThat(fillingStationDto.diesel()).isEqualTo(fillingStation.diesel());
    assertThat(fillingStationDto.gas()).isEqualTo(fillingStation.gas());
    assertThat(fillingStationDto.latitude()).isEqualTo(fillingStation.latitude());
    assertThat(fillingStationDto.longitude()).isEqualTo(fillingStation.longitude());
  }

  @Test
  @DisplayName("Should map FillingStationDto to null on null FillingStation")
  void shouldMapFillingStationDtoToNullOnNullFillingStation() {
    final FillingStationDto result = FillingStationDtoMapper.toDto((FillingStation) null);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("Should map list of FillingStationDto to null on null list of FillingStation")
  void shouldMapListOfFillingStationDtoToNullOnNullListOfFillingStation() {
    final List<FillingStationDto> result = FillingStationDtoMapper.toDto((List<FillingStation>) null);

    assertThat(result).isNull();
  }
}