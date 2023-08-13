package md.bot.fuel.rest.exception;

import static java.util.Collections.singletonList;
import static md.bot.fuel.rest.exception.ErrorConstants.ERROR_REASON_BIND_ERROR;
import static md.bot.fuel.rest.exception.ErrorConstants.ERROR_REASON_INTERNAL_ERROR;
import static md.bot.fuel.rest.exception.ErrorConstants.REST_CLIENT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

import md.bot.fuel.domain.exception.RfcError;
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
import org.springframework.web.context.request.WebRequest;

@Component
@ConditionalOnProperty(name = "app.error.strategy", havingValue = "RFC7807")
public class Rfc7807ErrorWrappingStrategy implements ErrorWrappingStrategy {

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
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
  public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception,
      WebRequest request) {
    final RfcErrorDescription error = buildRfcErrorDescription(exception.getStatus().value(), exception.getReasonCode(),
        exception.getMessage());
    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleExecutionException(ExecutionException exception, WebRequest request) {
    final RfcErrorDescription error = buildRfcErrorDescription(exception.getStatus().value(), exception.getReasonCode(),
        exception.getMessage());
    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception,
      WebRequest request) {
    final RfcErrorDescription error = buildRfcErrorDescription(exception.getStatus().value(), exception.getReasonCode(),
        exception.getMessage());
    return buildResponseEntity(exception.getStatus(), error);
  }

  @Override
  public ResponseEntity<ErrorDescriptionResponse> handleBindException(BindException exception, WebRequest request) {
    final RfcErrorDescription error = buildRfcErrorDescription(BAD_REQUEST.value(), ERROR_REASON_BIND_ERROR,
        exception.getMessage());
    return buildResponseEntity(BAD_REQUEST, error);
  }

  @Override
  public String getClient() {
    return REST_CLIENT;
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
