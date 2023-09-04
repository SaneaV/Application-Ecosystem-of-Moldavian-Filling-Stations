package md.fuel.bot.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import md.fuel.bot.infrastructure.repository.ErrorDescriptionResponse;
import md.fuel.bot.infrastructure.repository.GatewayError;

@Getter
public class GatewayErrorDescription<T extends GatewayError> implements ErrorDescriptionResponse {

  @Getter(onMethod_ = {@JsonGetter("Errors")})
  private final Errors<T> errors = new Errors<>();
}