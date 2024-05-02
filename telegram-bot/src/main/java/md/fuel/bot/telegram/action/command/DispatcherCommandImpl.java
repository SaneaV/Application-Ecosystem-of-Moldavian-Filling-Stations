package md.fuel.bot.telegram.action.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.telegram.action.DispatcherCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class DispatcherCommandImpl implements DispatcherCommand {

  private static final String COMMAND_NOT_FOUND = "I don't understand your command, use the menu buttons.";

  private final List<Command> commands;
  private final UpdateRadiusCommand updateRadiusCommand;
  private final UpdateCoordinatesCommand updateCoordinatesCommand;

  public List<? super BotApiMethod<?>> getMessages(Update update) {
    final String message = update.getMessage().getText();

    if (update.getMessage().hasLocation()) {
      log.info("Update user location");
      return updateCoordinatesCommand.execute(update);
    }

    if (message.length() != 0 && isDouble(message)) {
      log.info("Update user radius");
      return updateRadiusCommand.execute(update);
    }

    return commands.stream()
        .filter(c -> !c.getCommands().isEmpty() && c.getCommands().contains(message))
        .findFirst()
        .orElseThrow(() -> new EntityNotFoundException(COMMAND_NOT_FOUND))
        .execute(update);
  }

  private boolean isDouble(String string) {
    try {
      Double.parseDouble(string);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }
}
