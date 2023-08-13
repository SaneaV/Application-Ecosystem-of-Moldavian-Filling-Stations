package md.bot.fuel.rest.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import md.bot.fuel.domain.exception.RfcError;
import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;

@Getter
@Builder
@JsonInclude(NON_NULL)
public class RfcErrorDescription implements ErrorDescriptionResponse {

  private final int status;
  private final String detail;
  private final String title;
  private final String type;
  private final String correlationId;
  private final List<RfcError> errorDetails;
}
