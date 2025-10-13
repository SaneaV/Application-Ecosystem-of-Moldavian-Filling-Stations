package md.fuel.api.infrastructure.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ValueOfEnumValidatorTest {

  private final ValueOfEnumValidator valueOfEnumValidator = new ValueOfEnumValidator();

  @Test
  @DisplayName("Should return false on null fuel type")
  void shouldReturnFalseOnNullFuelType() {
    final boolean result = valueOfEnumValidator.isValid(null, null);
    assertThat(result).isFalse();
  }
}