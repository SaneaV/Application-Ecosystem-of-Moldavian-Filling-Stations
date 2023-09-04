package md.fuel.bot.infrastructure.exception;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.fuel.bot.telegram.exception.model.ClientRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ErrorWrappingStrategy errorWrappingStrategy;

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception, WebRequest request) {
    return errorWrappingStrategy.handleRuntimeException(exception, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleEntityNotFoundException(exception, request);
  }

  @ExceptionHandler(InfrastructureException.class)
  public ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception, WebRequest request) {
    return errorWrappingStrategy.handleInfrastructureException(exception, request);
  }

  @ExceptionHandler(GatewayPassThroughException.class)
  public ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleGatewayPassThroughException(exception, request);
  }

  @ExceptionHandler(ClientRequestException.class)
  public ResponseEntity<BotApiMethod<?>> handleClientRequestException(ClientRequestException exception, WebRequest request) {
    return errorWrappingStrategy.handleClientRequestException(exception, request);
  }
}
