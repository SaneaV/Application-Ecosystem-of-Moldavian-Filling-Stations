package md.fuel.api.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

public interface ErrorWrappingStrategy {

  ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception, WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
      WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception,
      WebRequest request);

  ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception, WebRequest request);
}
