package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;

@Getter
public class InfrastructureException extends RuntimeException {

  public InfrastructureException(String message) {
    super(message);
  }
}
