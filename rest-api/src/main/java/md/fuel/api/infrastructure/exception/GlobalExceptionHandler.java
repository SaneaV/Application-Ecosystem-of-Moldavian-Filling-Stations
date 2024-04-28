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

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ErrorWrappingStrategy errorWrappingStrategy;

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception) {
    log.info("Exception handler caught RuntimeException");
    return errorWrappingStrategy.handleRuntimeException(exception);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
    log.info("Exception handler caught EntityNotFoundException");
    return errorWrappingStrategy.handleEntityNotFoundException(exception);
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception) {
    log.info("Exception handler caught InvalidRequestException");
    return errorWrappingStrategy.handleInvalidRequestException(exception);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    log.info("Exception handler caught MethodArgumentNotValidException");
    return errorWrappingStrategy.handleMethodArgumentNotValidException(exception);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception) {
    log.info("Exception handler caught ConstraintViolationException");
    return errorWrappingStrategy.handleConstraintViolationException(exception);
  }

  @ExceptionHandler(InfrastructureException.class)
  public ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception) {
    log.info("Exception handler caught InfrastructureException");
    return errorWrappingStrategy.handleInfrastructureException(exception);
  }
}
