package md.fuel.bot.telegram.action.callback.type;

import java.util.List;
import java.util.Map;

public interface CallbackType {

  void initialize(String[] callbackData, Map<String, String> callbackMap);

  List<String> getCallbackType();
}
