package md.fuel.bot.infrastructure.exception;

import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.ExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface ErrorWrappingStrategy {

  ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception, WebRequest request);

  ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request);

  ResponseEntity<BotApiMethod<?>> handleExecutionException(ExecutionException exception, WebRequest request);
}
