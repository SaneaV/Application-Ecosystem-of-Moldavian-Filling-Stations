package md.fuel.bot.telegram.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.model.ClientRequestException;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.telegram.lib.exception.TelegramException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final TelegramWrappingStrategy errorWrappingStrategy;

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception) {
    log.info("Exception handler caught RuntimeException: {}", exception.getMessage());
    return errorWrappingStrategy.handleRuntimeException(exception);
  }

  @ExceptionHandler(TelegramException.class)
  public ResponseEntity<BotApiMethod<?>> handleTelegramException(TelegramException exception) {
    log.info("Exception handler caught TelegramException: {}", exception.getMessage());
    return errorWrappingStrategy.handleTelegramException(exception);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception) {
    log.info("Exception handler caught EntityNotFoundException");
    return errorWrappingStrategy.handleEntityNotFoundException(exception);
  }

  @ExceptionHandler(InfrastructureException.class)
  public ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception) {
    log.info("Exception handler caught InfrastructureException");
    return errorWrappingStrategy.handleInfrastructureException(exception);
  }

  @ExceptionHandler(GatewayPassThroughException.class)
  public ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception) {
    log.info("Exception handler caught GatewayPassThroughException");
    return errorWrappingStrategy.handleGatewayPassThroughException(exception);
  }

  @ExceptionHandler(ClientRequestException.class)
  public ResponseEntity<BotApiMethod<?>> handleClientRequestException(ClientRequestException exception) {
    log.info("Exception handler caught GatewayPassThroughException");
    return errorWrappingStrategy.handleClientRequestException(exception);
  }
}
