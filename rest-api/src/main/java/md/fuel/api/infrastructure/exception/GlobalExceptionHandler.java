package md.fuel.api.infrastructure.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ErrorWrappingStrategy errorWrappingStrategy;

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
    log.info("Exception handler caught RuntimeException");
    return errorWrappingStrategy.handleRuntimeException(exception, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception,
      WebRequest request) {
    log.info("Exception handler caught EntityNotFoundException");
    return errorWrappingStrategy.handleEntityNotFoundException(exception, request);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception,
      WebRequest request) {
    log.info("Exception handler caught InvalidRequestException");
    return errorWrappingStrategy.handleInvalidRequestException(exception, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
      WebRequest request) {
    log.info("Exception handler caught MethodArgumentNotValidException");
    return errorWrappingStrategy.handleMethodArgumentNotValidException(exception, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception,
      WebRequest request) {
    log.info("Exception handler caught ConstraintViolationException");
    return errorWrappingStrategy.handleConstraintViolationException(exception, request);
  }

  @ExceptionHandler(InfrastructureException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception,
      WebRequest request) {
    log.info("Exception handler caught InfrastructureException");
    return errorWrappingStrategy.handleInfrastructureException(exception, request);
  }
}
