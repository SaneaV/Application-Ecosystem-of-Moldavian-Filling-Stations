package md.fuel.bot.telegram.action.command;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

  List<? super PartialBotApiMethod<?>> execute(Update update);

  List<String> getCommands();
}
