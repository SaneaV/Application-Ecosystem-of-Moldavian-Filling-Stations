package md.bot.fuel.domain.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonInclude(NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class GatewayError {

  private String source;
  private String reasonCode;
  private String description;
  private boolean recoverable;

  /*
      Always null, present for backward compatibility
  */
  private static String DETAILS = null;
}
