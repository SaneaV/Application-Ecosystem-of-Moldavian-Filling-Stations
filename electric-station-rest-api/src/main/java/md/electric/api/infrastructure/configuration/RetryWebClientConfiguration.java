package md.electric.api.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.electric.api.infrastructure.exception.model.InfrastructureException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RetryWebClientConfiguration {

  private static final String ERROR_MESSAGE = "Request timed out.";
  private static final String ERROR_REASON_CODE = "REQUEST_TIMEOUT";

  private final ClientConfiguration configuration;

  public Retry fixedRetry() {
    return Retry.fixedDelay(configuration.getRetryCount(), configuration.getTimeDuration())
        .filter(this::worthRetrying)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
          log.error("After {} retries with {} timeout duration, API did not return any response", configuration.getRetryCount(),
              configuration.getTimeDuration());
          throw new InfrastructureException(ERROR_MESSAGE, ERROR_REASON_CODE);
        });
  }

  private boolean worthRetrying(Throwable throwable) {
    if (throwable instanceof WebClientResponseException responseException) {
      log.info("Check if request response contains in retry acceptable list");
      return configuration.getRetryable().contains(responseException.getStatusCode().value());
    }

    log.error("Can not retry request, not web client response exception");
    return false;
  }
}