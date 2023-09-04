package md.fuel.bot.telegram.exception;

import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import md.fuel.bot.infrastructure.exception.ErrorWrappingStrategy;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class TelegramExceptionWrappingStrategy implements ErrorWrappingStrategy {

  private static final String CHAT_ID_ATTRIBUTE = "chatId";
  private static final String UNKNOWN_ERROR = "Unknown error. Please contact bot administrator";

  @Override
  public ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception, WebRequest request) {
    return prepareErrorMessage(UNKNOWN_ERROR, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception,
      WebRequest request) {
    return prepareErrorMessage(exception.getMessage(), request);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception, WebRequest request) {
    return prepareErrorMessage(exception.getMessage(), request);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception,
      WebRequest request) {
    final String exceptionMessage = exception.getGatewayError().getErrors().getError().get(0).getDescription();
    return prepareErrorMessage(exceptionMessage, request);
  }

  private ResponseEntity<BotApiMethod<?>> prepareErrorMessage(String errorText, WebRequest request) {
    final Long chatId = getChatId(request);
    final SendMessage errorMessage = sendMessage(chatId, errorText, getMainMenuKeyboard());
    return ResponseEntity.ok().body(errorMessage);
  }

  private Long getChatId(WebRequest request) {
    return (Long) request.getAttribute(CHAT_ID_ATTRIBUTE, SCOPE_REQUEST);
  }
}
