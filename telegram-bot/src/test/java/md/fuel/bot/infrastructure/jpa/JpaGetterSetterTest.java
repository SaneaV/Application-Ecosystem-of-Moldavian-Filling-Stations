package md.fuel.bot.infrastructure.jpa;

import java.util.stream.Stream;
import md.fuel.bot.domain.GetterSetterTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class JpaGetterSetterTest extends GetterSetterTest {

  public static Stream<Arguments> getData() {
    return Stream.of(Arguments.of(new UserDataJpa()));
  }

  @ParameterizedTest
  @MethodSource("getData")
  public void testJpa(Object jpa) throws Exception {
    this.testGettersAndSetters(jpa);
  }
}