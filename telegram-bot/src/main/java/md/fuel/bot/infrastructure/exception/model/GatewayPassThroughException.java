package md.fuel.bot.infrastructure.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.exception.GatewayErrorDescription;
import md.fuel.bot.infrastructure.repository.GatewayError;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class GatewayPassThroughException extends RuntimeException {

  private final GatewayErrorDescription<GatewayError> gatewayError;
  private final HttpStatus httpStatus;
}