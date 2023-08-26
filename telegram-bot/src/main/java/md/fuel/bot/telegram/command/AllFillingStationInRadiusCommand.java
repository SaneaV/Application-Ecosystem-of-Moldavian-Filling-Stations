package md.fuel.bot.telegram.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.MessageUtil.sendLocation;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;

import java.util.ArrayList;
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
public class AllFillingStationInRadiusCommand implements Command {

  public static final String COMMAND = "All filling stations";

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final Message message = update.getMessage();
    final long userId = message.getFrom().getId();
    final long chatId = message.getChatId();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final List<FillingStation> allFillingStations = fillingStationFacade.getAllFillingStations(userData.latitude(),
        userData.longitude(), userData.radius(), FILLING_STATIONS_LIMIT, FILLING_STATIONS_LIMIT);

    return populateMessageMap(allFillingStations, chatId);
  }

  private List<? super PartialBotApiMethod<?>> populateMessageMap(List<FillingStation> allFillingStations, Long chatId) {
    final List<? super PartialBotApiMethod<?>> messages = new ArrayList<>();
    allFillingStations.forEach(fillingStation -> {
      final String messageText = toMessage(fillingStation);
      final SendMessage fillingStationMessage = sendMessage(chatId, messageText);
      final SendLocation fillingStationLocation = sendLocation(chatId, fillingStation.latitude(), fillingStation.longitude());
      messages.add(fillingStationMessage);
      messages.add(fillingStationLocation);
    });
    return messages;
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
