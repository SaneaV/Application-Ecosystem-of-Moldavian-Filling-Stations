package md.fuel.bot.infrastructure.exception;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class ExceptionUtils {

  private static final String INTEGRATION_SERVER_ERROR_MESSAGE = "An error occurred when calling external service.";

  public static void raiseException(Throwable throwable) {
    if (throwable instanceof InfrastructureException exception) {
      log.debug("Wrap InfrastructureException received on API request");
      throw exception;
    }
    if (throwable instanceof GatewayPassThroughException exception) {
      log.debug("Wrap GatewayPassThroughException received on API request");
      throw exception;
    }
    throw new InfrastructureException(INTEGRATION_SERVER_ERROR_MESSAGE);
  }
}