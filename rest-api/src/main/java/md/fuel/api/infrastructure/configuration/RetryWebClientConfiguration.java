package md.fuel.api.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
@RequiredArgsConstructor
public class RetryWebClientConfiguration {

  private static final String ERROR_MESSAGE = "Request timed out.";
  private static final String ERROR_REASON_CODE = "REQUEST_TIMEOUT";

  private final ApiConfiguration configuration;

  public Retry fixedRetry() {
    return Retry.fixedDelay(configuration.getRetryCount(), configuration.getTimeDuration())
        .filter(this::worthRetrying)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new InfrastructureException(ERROR_MESSAGE, ERROR_REASON_CODE));
  }

  private boolean worthRetrying(Throwable throwable) {
    final WebClientResponseException responseException = (WebClientResponseException) throwable;
    return configuration.getRetryable().contains(responseException.getRawStatusCode());
  }
}
