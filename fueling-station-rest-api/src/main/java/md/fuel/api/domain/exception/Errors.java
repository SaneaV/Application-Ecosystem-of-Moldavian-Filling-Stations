package md.fuel.api.domain.exception;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonGetter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Errors list wrapper.")
public class Errors {

  @Getter(onMethod_ = {@JsonGetter("Error")})
  @Schema(description = "List of gateway errors.", requiredMode = REQUIRED)
  private final List<GatewayError> error = new ArrayList<>();

  public void addError(GatewayError error) {
    this.error.add(error);
  }
}
