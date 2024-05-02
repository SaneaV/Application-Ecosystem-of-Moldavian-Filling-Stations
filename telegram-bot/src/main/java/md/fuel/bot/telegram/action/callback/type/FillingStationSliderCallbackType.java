package md.fuel.bot.telegram.action.callback.type;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FillingStationSliderCallbackType implements CallbackType {

  private static final String SHOW_LOCATION = "SHOW_LOCATION";
  private static final String BACK_TO_MENU = "BACK_TO_MENU";
  private static final String NEXT_PAGE = "NEXT_PAGE";
  private static final String PREVIOUS_PAGE = "PREVIOUS_PAGE";
  private static final String COMMAND = "COMMAND";
  private static final String OFFSET = "OFFSET";
  private static final String FUEL_TYPE = "FUEL_TYPE";

  @Override
  public void initialize(String[] callbackData, Map<String, String> callbackMap) {
    callbackMap.put(COMMAND, callbackData[1]);
    callbackMap.put(OFFSET, callbackData[2]);

    if (callbackData.length == 4) {
      callbackMap.put(FUEL_TYPE, callbackData[3]);
    }
  }

  @Override
  public List<String> getCallbackType() {
    return List.of(NEXT_PAGE, PREVIOUS_PAGE, SHOW_LOCATION, BACK_TO_MENU);
  }
}
