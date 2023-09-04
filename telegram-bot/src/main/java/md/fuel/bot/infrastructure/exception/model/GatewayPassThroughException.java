package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;
import md.fuel.bot.infrastructure.exception.GatewayErrorDescription;
import md.fuel.bot.infrastructure.repository.GatewayError;
import org.springframework.http.HttpStatus;

@Getter
public class GatewayPassThroughException extends RuntimeException {

  private final GatewayErrorDescription<GatewayError> gatewayError;
  private final HttpStatus httpStatus;

  public GatewayPassThroughException(GatewayErrorDescription<GatewayError> gatewayError, HttpStatus httpStatus) {
    this.gatewayError = gatewayError;
    this.httpStatus = httpStatus;
  }
}