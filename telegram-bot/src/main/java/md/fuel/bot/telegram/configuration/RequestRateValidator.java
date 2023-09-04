package md.fuel.bot.telegram.configuration;

import static java.util.Objects.isNull;

import com.github.benmanes.caffeine.cache.LoadingCache;
import md.fuel.bot.telegram.exception.model.ClientRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestRateValidator {

  private static final String ERROR_MESSAGE_LIMIT_EXCEEDED = """
      You have made too many requests lately. Wait a couple of seconds and try again.""";
  private static final int DEFAULT_REQUESTS_VALUE = 0;

  private final LoadingCache<Long, Integer> requestCountsPerUser;
  private final int maxRequestsPerSecond;

  public RequestRateValidator(LoadingCache<Long, Integer> requestCountsPerUser,
      @Value("${telegram.bot.requests-per-second}") int maxRequestsPerSecond) {
    this.requestCountsPerUser = requestCountsPerUser;
    this.maxRequestsPerSecond = maxRequestsPerSecond;
  }

  public void validateRequest(Long userId) {
    if (isMaximumRequestsPerSecondExceeded(userId)) {
      throw new ClientRequestException(ERROR_MESSAGE_LIMIT_EXCEEDED);
    }
  }

  private boolean isMaximumRequestsPerSecondExceeded(Long userId) {
    Integer requests = getOrDefault(userId);
    if (requests >= maxRequestsPerSecond) {
      requestCountsPerUser.put(userId, requests);
      return true;
    }
    requestCountsPerUser.put(userId, ++requests);
    return false;
  }

  private Integer getOrDefault(Long userId) {
    final Integer requests = requestCountsPerUser.get(userId);
    return isNull(requests) ? DEFAULT_REQUESTS_VALUE : requests;
  }
}