package md.fuel.api.rest.exception;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import md.fuel.api.domain.exception.Errors;
import md.fuel.api.infrastructure.exception.ErrorDescriptionResponse;

@Getter
@Schema(description = "Gateway error list wrapper.")
public class GatewayErrorDescription implements ErrorDescriptionResponse {

  @Schema(description = "Error list wrapper.", requiredMode = REQUIRED)
  @Getter(onMethod_ = {@JsonGetter("Errors")})
  private final Errors errors = new Errors();
}
