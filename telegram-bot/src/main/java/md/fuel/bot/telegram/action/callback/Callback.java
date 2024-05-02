package md.fuel.bot.telegram.action.callback;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface Callback {

  List<? super PartialBotApiMethod<?>> execute(CallbackQuery callbackQuery);

  String getCallbackType();
}
