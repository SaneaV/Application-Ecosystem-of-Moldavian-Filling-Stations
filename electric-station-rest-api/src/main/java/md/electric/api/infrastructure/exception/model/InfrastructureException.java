package md.electric.api.infrastructure.exception.model;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InfrastructureException extends RuntimeException {

  private final HttpStatus status = BAD_REQUEST;
  private final String reasonCode;

  public InfrastructureException(String message, String reasonCode) {
    super(message);
    this.reasonCode = reasonCode;
  }
}