package md.bot.fuel.telegram.command;

import static java.util.Collections.singletonList;
import static md.bot.fuel.telegram.converter.MessageConverter.toMessage;
import static md.bot.fuel.telegram.utils.MessageUtil.sendLocation;
import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;

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
public class AllInRadiusFuelStationCommand implements Command {

  public static final String COMMAND = "All fuel stations";

  private final FuelStationFacade fuelStationFacade;
  private final UserDataFacade userDataFacade;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final Message message = update.getMessage();
    final long userId = message.getFrom().getId();
    final long chatId = message.getChatId();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final List<FuelStationDto> allFuelStations = fuelStationFacade.getAllFuelStations(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius(), FUEL_STATIONS_LIMIT);

    final List<? super PartialBotApiMethod<?>> messages = new ArrayList<>();
    allFuelStations.forEach(fuelStation -> {
      final String messageText = toMessage(fuelStation);
      final SendMessage fuelStationMessage = sendMessage(chatId, messageText);
      final SendLocation fuelStationLocation = sendLocation(chatId, fuelStation.getLatitude(), fuelStation.getLongitude());
      messages.add(fuelStationMessage);
      messages.add(fuelStationLocation);
    });

    return messages;
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
