package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(String message) {
    super(message);
  }
}