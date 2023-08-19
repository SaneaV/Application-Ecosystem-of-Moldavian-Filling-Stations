package md.fuel.api.domain.exception;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Error in RFC format.")
public class RfcError {

  @Schema(description = "Error source.", example = "MD_FUEL_APP", requiredMode = REQUIRED)
  private final String source;
  @Schema(description = "Error reason code.", example = "NOT_FOUND", requiredMode = REQUIRED)
  private final String reason;
  @Schema(description = "Error description.", example = "Not Found.", requiredMode = REQUIRED)
  private final String message;
  @Schema(description = "Error recoverable status.", example = "true", requiredMode = REQUIRED)
  private final boolean recoverable;
}
