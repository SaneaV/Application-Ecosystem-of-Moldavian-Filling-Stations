package md.fuel.bot.telegram.command;

import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.MessageUtil.sendLocation;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.ArrayList;
import java.util.List;
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
public class BestFuelInRadiusCommand implements Command {

  public static List<String> COMMAND;

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;

  public BestFuelInRadiusCommand(FillingStationFacade fillingStationFacade, UserDataFacade userDataFacade,
      ChatInfoHolder chatInfoHolder) {
    this.fillingStationFacade = fillingStationFacade;
    this.userDataFacade = userDataFacade;
    this.chatInfoHolder = chatInfoHolder;
    COMMAND = fillingStationFacade.getSupportedFuelTypes().getFuelTypes();
  }

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    log.info("Get all filling stations in radius with best fuel price for user = {}", userId);

    final String fuelType = update.getMessage().getText();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final List<FillingStation> bestPriceFillingStations = fillingStationFacade.getBestFuelPrice(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius(), FILLING_STATIONS_LIMIT, FILLING_STATIONS_LIMIT, fuelType);

    final List<? super PartialBotApiMethod<?>> messages = populateMessageMap(bestPriceFillingStations, fuelType,
        chatInfoHolder.getChatId());
    setReplyKeyboard(messages);
    return messages;
  }

  private List<? super PartialBotApiMethod<?>> populateMessageMap(List<FillingStation> bestPriceFillingStations, String fuelType,
      Long chatId) {
    final List<? super PartialBotApiMethod<?>> messages = new ArrayList<>();
    bestPriceFillingStations.forEach(fuelStation -> {
      final String messageText = toMessage(fuelStation, fuelType);
      final SendMessage fuelStationMessage = sendMessage(chatId, messageText);
      final SendLocation fuelStationLocation = sendLocation(chatId, fuelStation.getLatitude(), fuelStation.getLongitude());
      messages.add(fuelStationMessage);
      messages.add(fuelStationLocation);
    });
    return messages;
  }

  private void setReplyKeyboard(List<? super PartialBotApiMethod<?>> messages) {
    final SendLocation sendLocation = (SendLocation) getLastMessage(messages);
    sendLocation.setReplyMarkup(getMainMenuKeyboard());
  }

  private Object getLastMessage(List<? super PartialBotApiMethod<?>> messages) {
    return messages.get(messages.size() - 1);
  }

  @Override
  public List<String> getCommands() {
    return COMMAND;
  }
}