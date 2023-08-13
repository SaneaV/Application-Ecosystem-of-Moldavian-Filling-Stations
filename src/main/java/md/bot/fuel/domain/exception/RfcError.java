package md.bot.fuel.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RfcError {

  private final String source;
  private final String reason;
  private final String message;
  private final boolean recoverable;
}
