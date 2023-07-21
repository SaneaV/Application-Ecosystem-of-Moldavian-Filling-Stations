package md.bot.fuel.infrastructure.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface ExceptionWrapper {

    ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception, WebRequest request);

    ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request);

    ResponseEntity<BotApiMethod<?>> handleExecutionException(ExecutionException exception, WebRequest request);

    ResponseEntity<BotApiMethod<?>> handleInvalidRequestException(InvalidRequestException exception, WebRequest request);
}
