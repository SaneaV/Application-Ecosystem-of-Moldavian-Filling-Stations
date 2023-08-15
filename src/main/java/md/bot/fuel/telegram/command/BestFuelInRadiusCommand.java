package md.bot.fuel.telegram.command;

import static java.util.Arrays.asList;
import static md.bot.fuel.domain.FuelType.DIESEL;
import static md.bot.fuel.domain.FuelType.GAS;
import static md.bot.fuel.domain.FuelType.PETROL;
import static md.bot.fuel.telegram.converter.MessageConverter.toMessage;
import static md.bot.fuel.telegram.utils.MessageUtil.sendLocation;
import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.UserDataFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.facade.dto.UserDataDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BestFuelInRadiusCommand implements Command {

  private final FuelStationFacade fuelStationFacade;
  private final UserDataFacade userDataFacade;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final Message message = update.getMessage();
    final long userId = message.getFrom().getId();
    final long chatId = message.getChatId();
    final String fuelType = message.getText();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final List<FuelStationDto> bestPriceFuelStations = fuelStationFacade.getBestFuelPrice(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius(), fuelType, FUEL_STATIONS_LIMIT);

    final List<? super PartialBotApiMethod<?>> messages = new ArrayList<>();
    bestPriceFuelStations.forEach(fuelStation -> {
      final String messageText = toMessage(fuelStation, fuelType);
      final SendMessage fuelStationMessage = sendMessage(chatId, messageText);
      final SendLocation fuelStationLocation = sendLocation(chatId, fuelStation.getLatitude(), fuelStation.getLongitude());
      messages.add(fuelStationMessage);
      messages.add(fuelStationLocation);
    });

    setReplyKeyboard(messages);
    return messages;
  }

  @Override
  public List<String> getCommands() {
    return asList(PETROL.getDescription(), DIESEL.getDescription(), GAS.getDescription());
  }

  private void setReplyKeyboard(List<? super PartialBotApiMethod<?>> messages) {
    final SendLocation sendLocation = (SendLocation) getLastMessage(messages);
    sendLocation.setReplyMarkup(getMainMenuKeyboard());
  }

  private Object getLastMessage(List<? super PartialBotApiMethod<?>> messages) {
    return messages.get(messages.size() - 1);
  }
}
