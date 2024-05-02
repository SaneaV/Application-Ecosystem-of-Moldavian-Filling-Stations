package md.fuel.bot.telegram.action;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface DispatcherCallback {

  List<? super BotApiMethod<?>> getMessages(CallbackQuery callbackQuery);
}