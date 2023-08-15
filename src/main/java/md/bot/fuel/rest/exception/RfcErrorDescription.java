package md.bot.fuel.rest.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import md.bot.fuel.domain.exception.RfcError;
import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;

@Getter
@Builder
@JsonInclude(NON_NULL)
@Schema(description = "RFC Errors wrapper.")
public class RfcErrorDescription implements ErrorDescriptionResponse {

  @Schema(description = "Error status.", example = "404", requiredMode = REQUIRED)
  private final int status;
  @Schema(description = "Error description.", example = "Not Found.", requiredMode = REQUIRED)
  private final String detail;
  @Schema(description = "Error reason code.", example = "NOT_FOUND", requiredMode = REQUIRED)
  private final String title;
  @Schema(description = "Error type.", example = "Error type.", requiredMode = NOT_REQUIRED)
  private final String type;
  @Schema(description = "Correlation id.", example = "Correlation id", requiredMode = NOT_REQUIRED)
  private final String correlationId;
  @Schema(description = "List of errors in RFC format.", requiredMode = NOT_REQUIRED)
  private List<RfcError> errorDetails;
}
