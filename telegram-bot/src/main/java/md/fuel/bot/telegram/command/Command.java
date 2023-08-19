package md.fuel.bot.telegram.command;

import java.util.List;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

  int FILLING_STATIONS_LIMIT = 10;

  List<? super PartialBotApiMethod<?>> execute(Update update);

  List<String> getCommands();
}
