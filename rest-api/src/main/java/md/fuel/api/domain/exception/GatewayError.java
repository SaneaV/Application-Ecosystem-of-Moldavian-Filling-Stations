package md.fuel.api.domain.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonInclude(NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Gateway error.")
public class GatewayError {

  @Schema(description = "Error source.", example = "MD_FUEL_APP", requiredMode = REQUIRED)
  private String source;
  @Schema(description = "Error reason code.", example = "NOT_FOUND", requiredMode = REQUIRED)
  private String reasonCode;
  @Schema(description = "Error description.", example = "Not Found.", requiredMode = REQUIRED)
  private String description;
  @Schema(description = "Error recoverable status.", example = "true", requiredMode = REQUIRED)
  private boolean recoverable;

  /*
      Always null, present for backward compatibility
  */
  private static String DETAILS = null;
}
