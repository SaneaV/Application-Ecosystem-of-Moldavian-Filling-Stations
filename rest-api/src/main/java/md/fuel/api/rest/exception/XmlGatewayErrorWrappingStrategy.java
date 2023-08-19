package md.fuel.api.rest.exception;

import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_BIND_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_CONSTRAINT_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_INTERNAL_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_SOURCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import java.util.List;
import javax.validation.ConstraintViolationException;
import md.fuel.api.domain.exception.GatewayError;
import md.fuel.api.infrastructure.exception.ErrorDescriptionResponse;
import md.fuel.api.infrastructure.exception.ErrorWrappingStrategy;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
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
