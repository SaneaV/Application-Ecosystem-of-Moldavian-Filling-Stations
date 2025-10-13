package md.fuel.api.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface ErrorWrappingStrategy {

  ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception);

  ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception);

  ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception);

  ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception);

  ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception);

  ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception);
}
