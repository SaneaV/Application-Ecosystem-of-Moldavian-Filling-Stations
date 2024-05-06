package md.telegram.lib.action;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface Callback {

  List<? extends PartialBotApiMethod<?>> execute(CallbackQuery callbackQuery);

  String getCallbackType();
}