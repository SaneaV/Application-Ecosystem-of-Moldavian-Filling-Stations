package md.bot.fuel.rest.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import md.bot.fuel.domain.exception.Errors;
import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;

@Getter
@Schema(description = "Gateway error list wrapper.")
public class GatewayErrorDescription implements ErrorDescriptionResponse {

  @Getter(onMethod_ = {@JsonGetter("Errors")})
  private final Errors errors = new Errors();
}
