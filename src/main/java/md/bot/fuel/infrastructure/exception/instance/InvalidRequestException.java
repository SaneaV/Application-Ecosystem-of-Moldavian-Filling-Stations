package md.bot.fuel.infrastructure.exception.instance;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequestException extends RuntimeException {

  private final HttpStatus status = BAD_REQUEST;
  private final String reasonCode;

  public InvalidRequestException(String message, String reasonCode) {
    super(message);
    this.reasonCode = reasonCode;
  }
}
