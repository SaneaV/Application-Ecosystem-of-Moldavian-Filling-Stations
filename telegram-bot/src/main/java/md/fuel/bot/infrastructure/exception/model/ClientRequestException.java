package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;

@Getter
public class ClientRequestException extends RuntimeException {

  public ClientRequestException(String message) {
    super(message);
  }
}