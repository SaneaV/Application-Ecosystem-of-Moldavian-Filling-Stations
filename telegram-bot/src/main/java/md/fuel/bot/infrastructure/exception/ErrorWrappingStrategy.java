package md.fuel.bot.infrastructure.exception;

import md.fuel.bot.infrastructure.exception.model.ClientRequestException;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface ErrorWrappingStrategy {

  ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception);

  ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception);

  ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception);

  ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception);

  ResponseEntity<BotApiMethod<?>> handleClientRequestException(ClientRequestException exception);
}
