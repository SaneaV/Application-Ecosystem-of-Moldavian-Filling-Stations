package md.fuel.bot.telegram.action.callback;

import static java.util.stream.Collectors.toMap;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.telegram.action.callback.type.CallbackType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackFactoryImpl implements CallbackFactory {

  private final List<CallbackType> callbackTypes;

  private Map<String, CallbackType> callbacksByType;

  @PostConstruct
  private void init() {
    callbacksByType = callbackTypes.stream()
        .flatMap(callbackType -> callbackType.getCallbackType().stream()
            .collect(toMap(Function.identity(), key -> callbackType, (a, b) -> b))
            .entrySet().stream())
        .collect(toMap(Entry::getKey, Entry::getValue));
  }

  @Override
  public CallbackType getCallback(String callbackType) {
    return callbacksByType.get(callbackType);
  }
}
