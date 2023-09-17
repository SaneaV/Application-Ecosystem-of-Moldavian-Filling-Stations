package md.fuel.bot.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class RetryWebClientConfiguration {

  private static final String ERROR_MESSAGE = "Request timed out.";

  private final ApiConfiguration configuration;

  public Retry fixedRetry() {
    return Retry.fixedDelay(configuration.getRetryCount(), configuration.getTimeDuration())
        .filter(this::worthRetrying)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new InfrastructureException(ERROR_MESSAGE));
  }

  private boolean worthRetrying(Throwable throwable) {
    if (throwable instanceof GatewayPassThroughException responseException) {
      return configuration.getRetryable().contains(responseException.getHttpStatus().value());
    }
    return false;
  }
}
