package md.bot.fuel.facade.dto;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.util.stream.Stream;
import md.bot.fuel.common.GetterSetterTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(PER_CLASS)
public class DtoGetterSetterTest extends GetterSetterTest {

  private static final double DEFAULT_DOUBLE = 0.0D;

  public static Stream<Arguments> getData() {
    return Stream.of(
        Arguments.of(new FuelStationDto(EMPTY, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE)));
  }

  @ParameterizedTest
  @MethodSource("getData")
  public void testDto(Object dto) throws Exception {
    this.testGettersAndSetters(dto);
  }
}