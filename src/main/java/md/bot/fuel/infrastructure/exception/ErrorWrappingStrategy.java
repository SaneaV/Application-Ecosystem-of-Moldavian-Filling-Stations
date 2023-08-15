package md.bot.fuel.infrastructure.exception;

import javax.validation.ConstraintViolationException;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.context.request.WebRequest;

public interface ErrorWrappingStrategy {

  ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleExecutionException(ExecutionException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleBindException(BindException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request);

  String getClient();
}
