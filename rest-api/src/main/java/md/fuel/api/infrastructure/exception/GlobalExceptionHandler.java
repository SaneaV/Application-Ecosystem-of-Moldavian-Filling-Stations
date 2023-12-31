package md.fuel.api.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ErrorWrappingStrategy errorWrappingStrategy;

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
    return errorWrappingStrategy.handleRuntimeException(exception, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleEntityNotFoundException(exception, request);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleInvalidRequestException(exception, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleMethodArgumentNotValidException(exception, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleConstraintViolationException(exception, request);
  }

  @ExceptionHandler(InfrastructureException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleInfrastructureException(exception, request);
  }
}
