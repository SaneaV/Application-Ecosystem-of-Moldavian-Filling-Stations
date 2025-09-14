package md.fuel.bot.infrastructure.configuration;

import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.model.ClientRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RequestRateValidator {

  private static final String ERROR_MESSAGE_LIMIT_EXCEEDED = "error.too-many-requests.message";

  private final LoadingCache<Long, Integer> requestCountsPerUser;
  private final int maxRequestsPerSecond;

  public RequestRateValidator(LoadingCache<Long, Integer> requestCountsPerUser,
      @Value("${telegram.bot.requests-per-second}") int maxRequestsPerSecond) {
    this.requestCountsPerUser = requestCountsPerUser;
    this.maxRequestsPerSecond = maxRequestsPerSecond;
  }

  public void validateRequest(Long userId) {
    if (isMaximumRequestsPerSecondExceeded(userId)) {
      log.debug("User {} exceeded request limit", userId);
      throw new ClientRequestException(ERROR_MESSAGE_LIMIT_EXCEEDED);
    }
  }

  private boolean isMaximumRequestsPerSecondExceeded(Long userId) {
    Integer requests = requestCountsPerUser.get(userId);
    if (requests >= maxRequestsPerSecond) {
      requestCountsPerUser.put(userId, requests);
      return true;
    }
    requestCountsPerUser.put(userId, ++requests);
    return false;
  }
}