package md.bot.fuel.infrastructure.exception;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorWrappingStrategyFactory {

  private static final String ERROR_DESCRIPTION = "Error wrapping strategy not found";
  private static final String ERROR_REASON_CODE = "ERROR_WRAPPING_STRATEGY_NOT_FOUND";

  private final List<ErrorWrappingStrategy> errorWrappingStrategies;
  private Map<String, ErrorWrappingStrategy> errorWrappingStrategiesByClient;

  @PostConstruct
  public void init() {
    errorWrappingStrategiesByClient = errorWrappingStrategies.stream()
        .collect(toMap(ErrorWrappingStrategy::getClient, Function.identity()));
  }

  public ErrorWrappingStrategy getErrorWrappingStrategy(String client) {
    final ErrorWrappingStrategy errorWrappingStrategy = errorWrappingStrategiesByClient.get(client);

    if (isNull(errorWrappingStrategy)) {
      throw new EntityNotFoundException(ERROR_DESCRIPTION, ERROR_REASON_CODE);
    }
    return errorWrappingStrategy;
  }
}
