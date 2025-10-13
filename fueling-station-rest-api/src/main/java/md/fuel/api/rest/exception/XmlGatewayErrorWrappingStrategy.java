package md.fuel.api.rest.exception;

import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_CONSTRAINT_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_INTERNAL_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_METHOD_ARGUMENT_NOT_VALID;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_SOURCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import md.fuel.api.domain.exception.GatewayError;
import md.fuel.api.infrastructure.exception.ErrorDescriptionResponse;
import md.fuel.api.infrastructure.exception.ErrorWrappingStrategy;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@ConditionalOnProperty(name = "app.error.strategy", havingValue = "XML")
public class XmlGatewayErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(ERROR_REASON_INTERNAL_ERROR, exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(INTERNAL_SERVER_ERROR, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(exception.getReasonCode(), exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(exception.getReasonCode(), exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final List<FieldError> fieldErrors = exception.getFieldErrors();

    fieldErrors.forEach(e -> {
      final GatewayError gatewayError = buildGatewayError(ERROR_REASON_METHOD_ARGUMENT_NOT_VALID, e.getDefaultMessage());
      error.getErrors().addError(gatewayError);
    });

    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(ERROR_REASON_CONSTRAINT_ERROR, exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception) {
    final GatewayErrorDescription error = new GatewayErrorDescription();
    final GatewayError gatewayError = buildGatewayError(exception.getReasonCode(), exception.getMessage());
    error.getErrors().addError(gatewayError);

    return buildResponseEntity(exception.getStatus(), error);
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
