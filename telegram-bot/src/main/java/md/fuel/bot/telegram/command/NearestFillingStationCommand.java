package md.fuel.bot.telegram.command;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.MessageUtil.sendLocation;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.telegram.dto.UserDataDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class NearestFillingStationCommand implements Command {

  public static final String COMMAND = "Nearest filling station";

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final Message message = update.getMessage();
    final long userId = message.getFrom().getId();
    final long chatId = message.getChatId();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final FillingStation nearestFuelStation = fillingStationFacade.getNearestFillingStation(userData.latitude(),
        userData.longitude(), userData.radius());

    final String fuelStationTextMessage = toMessage(nearestFuelStation);
    final SendMessage fuelStationMessage = sendMessage(chatId, fuelStationTextMessage, getMainMenuKeyboard());
    final SendLocation fuelStationLocation = sendLocation(chatId, nearestFuelStation.latitude(), nearestFuelStation.longitude());

    return asList(fuelStationMessage, fuelStationLocation);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}