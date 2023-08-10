package md.bot.fuel.infrastructure.exception;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ErrorWrappingStrategyFactoryTest {

  private static final String NON_EXISTING_ERROR_WRAPPING_STRATEGY = "RANDOM CLIENT";
  private static final String ERROR_DESCRIPTION = "Error wrapping strategy not found";

  private final ErrorWrappingStrategyFactory errorWrappingStrategyFactory;

  public ErrorWrappingStrategyFactoryTest() {
    ErrorWrappingStrategy errorWrappingStrategy = mock(ErrorWrappingStrategy.class);
    this.errorWrappingStrategyFactory = new ErrorWrappingStrategyFactory(singletonList(errorWrappingStrategy));
  }

  @Test
  @DisplayName("Should throw exception on non-existing wrapping strategy")
  void shouldThrowException() {
    errorWrappingStrategyFactory.init();
    assertThatThrownBy(() -> errorWrappingStrategyFactory.getErrorWrappingStrategy(NON_EXISTING_ERROR_WRAPPING_STRATEGY))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(ERROR_DESCRIPTION);
  }
}
