package md.fuel.bot.telegram.action;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface DispatcherCommand {

  List<? super BotApiMethod<?>> getMessages(Update update);
}