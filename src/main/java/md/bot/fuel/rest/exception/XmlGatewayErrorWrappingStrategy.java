package md.bot.fuel.rest.exception;

import static md.bot.fuel.rest.exception.ErrorConstants.ERROR_REASON_BIND_ERROR;
import static md.bot.fuel.rest.exception.ErrorConstants.ERROR_REASON_CONSTRAINT_ERROR;
import static md.bot.fuel.rest.exception.ErrorConstants.ERROR_REASON_INTERNAL_ERROR;
import static md.bot.fuel.rest.exception.ErrorConstants.ERROR_SOURCE;
import static md.bot.fuel.rest.exception.ErrorConstants.REST_CLIENT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import java.util.List;
import javax.validation.ConstraintViolationException;
import md.bot.fuel.domain.exception.GatewayError;
import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;
import md.bot.fuel.infrastructure.exception.ErrorWrappingStrategy;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

@Component
@ConditionalOnProperty(name = "app.error.strategy", havingValue = "XML")
public class XmlGatewayErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(ERROR_REASON_INTERNAL_ERROR, exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(INTERNAL_SERVER_ERROR, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception,
      WebRequest request) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(exception.getReasonCode(), exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleExecutionException(ExecutionException exception, WebRequest request) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(exception.getReasonCode(), exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception,
      WebRequest request) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(exception.getReasonCode(), exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleBindException(BindException exception, WebRequest request) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final List<FieldError> fieldErrors = exception.getFieldErrors();

    fieldErrors.forEach(e -> {
      final GatewayError gatewayError = buildGatewayError(ERROR_REASON_BIND_ERROR, e.getDefaultMessage());
      error.getErrors().addError(gatewayError);
    });

    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception,
      WebRequest request) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(ERROR_REASON_CONSTRAINT_ERROR, exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public String getClient() {
    return REST_CLIENT;
  }

  private GatewayError buildGatewayError(String reasonCode, String description) {
    return GatewayError.builder()
        .source(ERROR_SOURCE)
        .reasonCode(reasonCode)
        .description(description)
        .recoverable(false)
        .build();
  }

  private ResponseEntity<ErrorDescriptionResponse> buildResponseEntity(HttpStatus status, ErrorDescriptionResponse response) {
    return ResponseEntity.status(status)
        .contentType(APPLICATION_PROBLEM_JSON)
        .body(response);
  }
}
