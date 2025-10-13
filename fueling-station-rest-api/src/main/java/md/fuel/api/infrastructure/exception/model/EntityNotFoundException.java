package md.fuel.api.infrastructure.exception.model;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityNotFoundException extends RuntimeException {

  private final HttpStatus status = NOT_FOUND;
  private final String reasonCode;

  public EntityNotFoundException(String message, String reasonCode) {
    super(message);
    this.reasonCode = reasonCode;
  }
}