package md.fuel.bot.telegram.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.facade.UserDataFacade;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

  private static final String COMMAND = "/start";
  private static final String MESSAGE = """
      Welcome!
      To start working with bot, you can select any element from the menu.

      If you want to change the search radius, just send it to me (in kilometres, e.g. 0.5 (500 metres), 1 (1000 metres)).

      If you want to change your coordinates, just send your location.""";

  private final UserDataFacade userDataFacade;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final Long userId = update.getMessage().getFrom().getId();
    log.info("Add new user with id = {}", userId);
    userDataFacade.addNewUser(userId);

    final SendMessage message = sendMessage(update, MESSAGE, getMainMenuKeyboard());
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
