package md.fuel.api.infrastructure.exception;

import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
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

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleBindException(BindException exception, WebRequest request) {
    return errorWrappingStrategy.handleBindException(exception, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception,
      WebRequest request) {
    return errorWrappingStrategy.handleConstraintViolationException(exception, request);
  }
}
