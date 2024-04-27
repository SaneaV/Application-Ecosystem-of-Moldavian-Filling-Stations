package md.fuel.bot.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RetryWebClientConfiguration {

  private static final String ERROR_MESSAGE = "Request timed out.";

  private final ApiConfiguration configuration;

  public Retry fixedRetry() {
    log.info("Can not fetch data from API, retry fetch...");

    return Retry.fixedDelay(configuration.getRetryCount(), configuration.getTimeDuration())
        .filter(this::worthRetrying)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
          log.error("After {} retries with {} timeout duration, API did not return any response", configuration.getRetryCount(),
              configuration.getTimeDuration());

          throw new InfrastructureException(ERROR_MESSAGE);
        });
  }

  private boolean worthRetrying(Throwable throwable) {
    if (throwable instanceof GatewayPassThroughException responseException) {
      log.info("Check if request response contains in retry acceptable list");
      return configuration.getRetryable().contains(responseException.getHttpStatus().value());
    }
    log.error("Can not retry request, not web client response exception");
    return false;
  }
}
