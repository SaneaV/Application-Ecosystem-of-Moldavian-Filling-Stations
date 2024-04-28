package md.fuel.bot.telegram.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.MessageUtil.sendLocation;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.dto.UserDataDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllFillingStationInRadiusCommand implements Command {

  public static final String COMMAND = "All filling stations";

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    log.info("Get all filling stations in radius for user = {}", userId);

    final UserDataDto userData = userDataFacade.getUserData(userId);
    final List<FillingStation> allFillingStations = fillingStationFacade.getAllFillingStations(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius(), FILLING_STATIONS_LIMIT, FILLING_STATIONS_LIMIT);

    return populateMessageMap(allFillingStations, chatInfoHolder.getChatId());
  }

  private List<? super PartialBotApiMethod<?>> populateMessageMap(List<FillingStation> allFillingStations, Long chatId) {
    final List<? super PartialBotApiMethod<?>> messages = new ArrayList<>();
    allFillingStations.forEach(fillingStation -> {
      final String messageText = toMessage(fillingStation);
      final SendMessage fillingStationMessage = sendMessage(chatId, messageText);
      final SendLocation fillingStationLocation = sendLocation(chatId, fillingStation.getLatitude(), fillingStation.getLongitude());
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
