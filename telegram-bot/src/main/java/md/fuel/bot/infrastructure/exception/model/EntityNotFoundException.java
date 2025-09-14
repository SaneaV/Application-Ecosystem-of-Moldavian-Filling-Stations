package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  private String exceptionCode;

  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, String exceptionCode) {
    super(message);
    this.exceptionCode = exceptionCode;
  }
}