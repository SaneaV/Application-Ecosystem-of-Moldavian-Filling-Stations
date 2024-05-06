package md.telegram.lib.action;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface DispatcherCallback {

  List<? extends PartialBotApiMethod<?>> getMessages(CallbackQuery callbackQuery);
}