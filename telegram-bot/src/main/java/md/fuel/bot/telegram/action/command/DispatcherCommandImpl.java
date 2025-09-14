package md.fuel.bot.telegram.action.command;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.telegram.lib.action.Command;
import md.telegram.lib.action.DispatcherCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class DispatcherCommandImpl implements DispatcherCommand {

  private static final String COMMAND_NOT_FOUND = "error.unknown-command.message";

  private final List<Command> commands;
  private final UpdateRadiusCommand updateRadiusCommand;
  private final UpdateCoordinatesCommand updateCoordinatesCommand;
  private final TranslatorService translatorService;
  private final UserDataFacade userDataFacade;

  public List<? extends PartialBotApiMethod<?>> getMessages(Update update) {
    final Message updateMessage = update.getMessage();
    final String message = updateMessage.getText();

    if (updateMessage.hasLocation()) {
      log.info("Update user location");
      return updateCoordinatesCommand.execute(update);
    }

    if (!message.isEmpty() && isDouble(message)) {
      log.info("Update user radius");
      return updateRadiusCommand.execute(update);
    }

    final Long userId = updateMessage.getFrom().getId();
    final String language = userDataFacade.getLanguage(userId);

    final Optional<Command> matchingCommand = commands.stream()
        .filter(command -> command.getCommands().stream()
            .anyMatch(c -> translatorService.translate(language, c).equalsIgnoreCase(message)))
        .findFirst();

    return matchingCommand
        .map(cmd -> cmd.execute(update))
        .orElseThrow(() -> {
          String messageText = translatorService.translate(language, COMMAND_NOT_FOUND);
          return new EntityNotFoundException(messageText);
        });
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
