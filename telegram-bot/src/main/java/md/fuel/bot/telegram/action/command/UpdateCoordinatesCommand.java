package md.fuel.bot.telegram.action.command;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.telegram.lib.action.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateCoordinatesCommand implements Command {

  private static final String MESSAGE = "New coordinates set!";

  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    log.info("Update coordinates for user with id = {}", userId);
    final Location userLocation = update.getMessage().getLocation();

    userDataFacade.updateCoordinates(userId, userLocation.getLatitude(), userLocation.getLongitude());

    final SendMessage sendMessage = sendMessage(chatInfoHolder.getChatId(), MESSAGE, getMainMenuKeyboard());
    return singletonList(sendMessage);
  }

  @Override
  public List<String> getCommands() {
    return emptyList();
  }
}
