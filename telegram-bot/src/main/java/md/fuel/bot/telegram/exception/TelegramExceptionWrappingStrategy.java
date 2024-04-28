package md.fuel.bot.telegram.exception;

import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.exception.ErrorWrappingStrategy;
import md.fuel.bot.infrastructure.exception.model.ClientRequestException;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class TelegramExceptionWrappingStrategy implements ErrorWrappingStrategy {

  private static final String UNKNOWN_ERROR = "Unknown error. Please contact bot administrator";

  private final ChatInfoHolder chatInfoHolder;

  @Override
  public ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception) {
    return prepareErrorMessage(UNKNOWN_ERROR);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception) {
    return prepareErrorMessage(exception.getMessage());
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception) {
    return prepareErrorMessage(exception.getMessage());
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception) {
    final String exceptionMessage = exception.getGatewayError().getErrors().getError().get(0).getDescription();
    return prepareErrorMessage(exceptionMessage);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleClientRequestException(ClientRequestException exception) {
    return prepareErrorMessage(exception.getMessage());
  }

  private ResponseEntity<BotApiMethod<?>> prepareErrorMessage(String errorText) {
    final SendMessage errorMessage = sendMessage(chatInfoHolder.getChatId(), errorText, getMainMenuKeyboard());
    return ResponseEntity.ok().body(errorMessage);
  }
}