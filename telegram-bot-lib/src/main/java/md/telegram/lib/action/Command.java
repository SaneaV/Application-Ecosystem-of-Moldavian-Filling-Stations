package md.telegram.lib.action;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

  List<? extends PartialBotApiMethod<?>> execute(Update update);

  List<String> getCommands();
}