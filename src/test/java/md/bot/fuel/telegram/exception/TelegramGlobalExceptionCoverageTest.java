package md.bot.fuel.telegram.exception;

import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.objects.Update;

// TODO: Remove this test class on module separation
public class TelegramGlobalExceptionCoverageTest {

  private final TelegramExceptionWrappingStrategy telegramExceptionWrappingStrategy;

  private static final String BIND_EXCEPTION_MESSAGE = "org.springframework.validation.BeanPropertyBindingResult: 0 errors";

  public TelegramGlobalExceptionCoverageTest() {
    this.telegramExceptionWrappingStrategy = new TelegramExceptionWrappingStrategy();
  }

  @Test
  @DisplayName("Should wrap bind exceptions")
  void shouldWrapBindExceptions() {
    final TelegramErrorDescription expected = new TelegramErrorDescription(null, BIND_EXCEPTION_MESSAGE, getMainMenuKeyboard());

    final BindException bindException = new BindException(Update.class, "update");
    final ResponseEntity<ErrorDescriptionResponse> response = telegramExceptionWrappingStrategy.handleBindException(bindException,
        mock(WebRequest.class));

    assertThat(response.getBody()).isEqualTo(expected);
  }
}
