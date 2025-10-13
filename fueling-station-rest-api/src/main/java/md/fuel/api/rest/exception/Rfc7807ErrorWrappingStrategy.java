package md.fuel.api.rest.exception;

import static java.util.Collections.singletonList;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_CONSTRAINT_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_INTERNAL_ERROR;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_REASON_METHOD_ARGUMENT_NOT_VALID;
import static md.fuel.api.rest.exception.ErrorConstants.ERROR_SOURCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import md.fuel.api.domain.exception.RfcError;
import md.fuel.api.infrastructure.exception.ErrorDescriptionResponse;
import md.fuel.api.infrastructure.exception.ErrorWrappingStrategy;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import md.fuel.api.infrastructure.exception.model.InvalidRequestException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
@ConditionalOnProperty(name = "app.error.strategy", havingValue = "RFC7807")
public class Rfc7807ErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception) {
    final RfcErrorDescription error = RfcErrorDescription.builder()
        .status(INTERNAL_SERVER_ERROR.value())
        .title(INTERNAL_SERVER_ERROR.getReasonPhrase())
        .errorDetails(singletonList(RfcError.builder()
            .reason(ERROR_REASON_INTERNAL_ERROR)
            .message(exception.getMessage())
            .recoverable(false)
            .build()))
        .build();
    return buildResponseEntity(INTERNAL_SERVER_ERROR, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
    final RfcErrorDescription error = buildRfcErrorDescription(exception.getStatus().value(), exception.getReasonCode(),
        exception.getMessage());
    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception) {
    final RfcErrorDescription error = buildRfcErrorDescription(exception.getStatus().value(), exception.getReasonCode(),
        exception.getMessage());
    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

    final List<RfcError> rfcErrors = exception.getFieldErrors().stream()
        .map(e -> RfcError.builder()
            .source(ERROR_SOURCE)
            .message(e.getDefaultMessage())
            .reason(ERROR_REASON_METHOD_ARGUMENT_NOT_VALID)
            .recoverable(false)
            .build())
        .toList();

    final RfcErrorDescription error = RfcErrorDescription.builder()
        .status(BAD_REQUEST.value())
        .title(ERROR_REASON_METHOD_ARGUMENT_NOT_VALID)
        .errorDetails(rfcErrors)
        .build();

    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleConstraintViolationException(ConstraintViolationException exception) {
    final RfcErrorDescription error = buildRfcErrorDescription(BAD_REQUEST.value(), ERROR_REASON_CONSTRAINT_ERROR,
        exception.getMessage());
    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleInfrastructureException(InfrastructureException exception) {
    final RfcErrorDescription error = buildRfcErrorDescription(exception.getStatus().value(), exception.getReasonCode(),
        exception.getMessage());
    return buildResponseEntity(exception.getStatus(), error);
  }

  private RfcErrorDescription buildRfcErrorDescription(int status, String title, String detail) {
    return RfcErrorDescription.builder()
        .status(status)
        .title(title)
        .detail(detail)
        .build();
  }

  private ResponseEntity<ErrorDescriptionResponse> buildResponseEntity(HttpStatus status, ErrorDescriptionResponse response) {
    return ResponseEntity.status(status)
        .contentType(APPLICATION_PROBLEM_JSON)
        .body(response);
  }
}
