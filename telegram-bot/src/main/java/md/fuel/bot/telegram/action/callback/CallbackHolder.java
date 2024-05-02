package md.fuel.bot.telegram.action.callback;

import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.telegram.action.callback.type.CallbackType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CallbackHolder {

  public static final InheritableThreadLocal<Map<String, String>> CALLBACK_DATA = new InheritableThreadLocal<>();
  public static final InheritableThreadLocal<String> CALLBACK_TYPE = new InheritableThreadLocal<>();
  public static final InheritableThreadLocal<Integer> MESSAGE_ID = new InheritableThreadLocal<>();

  static {
    CALLBACK_DATA.set(new HashMap<>());
  }

  private final CallbackFactory callbackFactory;

  public void parse(String callbackData, Integer messageId) {
    final String[] callbackDataSplit = callbackData.split(SPACE);
    CALLBACK_TYPE.set(callbackDataSplit[0]);
    MESSAGE_ID.set(messageId);

    final CallbackType callback = callbackFactory.getCallback(getCallbackType());
    callback.initialize(callbackDataSplit, CALLBACK_DATA.get());
  }

  public String getCallbackDataBy(String callbackParameter) {
    return CALLBACK_DATA.get().get(callbackParameter);
  }

  public String getCallbackType() {
    return CALLBACK_TYPE.get();
  }

  public Integer getMessageId() {
    return MESSAGE_ID.get();
  }
}
