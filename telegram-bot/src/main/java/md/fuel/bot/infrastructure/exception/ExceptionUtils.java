package md.fuel.bot.infrastructure.exception;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;

@NoArgsConstructor(access = PRIVATE)
public class ExceptionUtils {

  private static final String INTEGRATION_SERVER_ERROR_MESSAGE = "An error occurred when calling external service.";

  public static void raiseException(Throwable throwable) {
    if (throwable instanceof InfrastructureException exception) {
      throw exception;
    }
    if (throwable instanceof GatewayPassThroughException exception) {
      throw exception;
    }
    throw new InfrastructureException(INTEGRATION_SERVER_ERROR_MESSAGE);
  }
}