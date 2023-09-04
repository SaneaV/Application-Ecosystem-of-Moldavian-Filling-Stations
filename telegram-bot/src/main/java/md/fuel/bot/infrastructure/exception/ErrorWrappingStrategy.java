package md.fuel.bot.infrastructure.exception;

import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.fuel.bot.telegram.exception.model.ClientRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface ErrorWrappingStrategy {

  ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception, WebRequest request);

  ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request);

  ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception, WebRequest request);

  ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception, WebRequest request);

  ResponseEntity<BotApiMethod<?>> handleClientRequestException(ClientRequestException exception, WebRequest request);
}
