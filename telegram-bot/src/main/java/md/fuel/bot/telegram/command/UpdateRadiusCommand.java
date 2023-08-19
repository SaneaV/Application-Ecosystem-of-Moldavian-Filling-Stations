package md.fuel.bot.telegram.command;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.facade.UserDataFacade;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateRadiusCommand implements Command {

  private static final String MESSAGE = "New radius set!";

  private final UserDataFacade userDataFacade;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final Message message = update.getMessage();
    final Long userId = message.getFrom().getId();
    final String newRadius = message.getText();

    userDataFacade.updateRadius(userId, Double.parseDouble(newRadius));

    final SendMessage sendMessage = sendMessage(update, MESSAGE, getMainMenuKeyboard());
    return singletonList(sendMessage);
  }

  @Override
  public List<String> getCommands() {
    return emptyList();
  }
}
