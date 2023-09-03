package md.fuel.api.domain.exception;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Error in RFC format.")
public record RfcError(
    @Schema(description = "Error source.", example = "MD_FUEL_APP", requiredMode = REQUIRED) String source,
    @Schema(description = "Error reason code.", example = "NOT_FOUND", requiredMode = REQUIRED) String reason,
    @Schema(description = "Error description.", example = "Not Found.", requiredMode = REQUIRED) String message,
    @Schema(description = "Error recoverable status.", example = "true", requiredMode = REQUIRED) boolean recoverable) {

}
