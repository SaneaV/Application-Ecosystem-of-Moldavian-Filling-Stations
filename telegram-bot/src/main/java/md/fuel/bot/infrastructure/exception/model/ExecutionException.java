package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;

@Getter
public class ExecutionException extends RuntimeException {

  public ExecutionException(String message) {
    super(message);
  }
}
