package md.bot.fuel.telegram.command;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.UserDataFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.facade.dto.UserDataDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class AllInRadiusFuelStationCommandTest {

  private static final String COMMAND = "All fuel stations";
  private static final String FUEL_STATION_MESSAGE = "⛽ Fuel station - \"%s\"\n\n"
      + "%s Petrol: %s lei\n"
      + "%s Diesel: %s lei\n"
      + "%s Gas : %s lei\n\n"
      + "📊 Last price update: %s";
  private static final String GREEN_CIRCLE = "🟢";
  private static final String RED_CIRCLE = "🔴";

  private final AllInRadiusFuelStationCommand allInRadiusFuelStationCommand;
  private final FuelStationFacade fuelStationFacade;
  private final UserDataFacade userDataFacade;

  public AllInRadiusFuelStationCommandTest() {
    this.fuelStationFacade = mock(FuelStationFacade.class);
    this.userDataFacade = mock(UserDataFacade.class);
    this.allInRadiusFuelStationCommand = new AllInRadiusFuelStationCommand(fuelStationFacade, userDataFacade);
  }

  @Test
  @DisplayName("Should return all fuel stations in radius message")
  void shouldReturnAllFuelStationsInRadiusMessage() {
    final long chatId = 20L;
    final long userId = 10L;
    final String fuelStationName = "Fuel Station Name";
    final double petrol = 10;
    final double diesel = 20;
    final Double gas = null;
    final double latitude = 40;
    final double longitude = 50;
    final double defaultDoubleValue = 0;
    final long defaultLongValue = 0L;

    final User user = new User();
    final Update update = new Update();
    final Message message = new Message();
    final Chat chat = new Chat();
    final FuelStationDto fuelStationDto = new FuelStationDto(fuelStationName, petrol, diesel, gas, latitude, longitude);
    FuelStationDto.timestamp = LocalDateTime.now().toString();
    final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue, defaultDoubleValue);
    final String messageText = String.format(FUEL_STATION_MESSAGE, fuelStationName, GREEN_CIRCLE, petrol, GREEN_CIRCLE, diesel,
        RED_CIRCLE, 0.0, FuelStationDto.timestamp);

    user.setId(userId);
    chat.setId(chatId);
    message.setFrom(user);
    message.setChat(chat);
    update.setMessage(message);

    when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
    when(fuelStationFacade.getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenReturn(
        singletonList(fuelStationDto));

    final List<? super PartialBotApiMethod<?>> sendMessagesAndLocations = allInRadiusFuelStationCommand.execute(update);

    assertThat(sendMessagesAndLocations).hasSize(2);
    final SendMessage sendMessage = (SendMessage) sendMessagesAndLocations.get(0);
    final SendLocation sendLocation = (SendLocation) sendMessagesAndLocations.get(1);
    assertThat(sendMessage.getText()).isEqualTo(messageText);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendLocation.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendLocation.getLatitude()).isEqualTo(fuelStationDto.getLatitude());
    assertThat(sendLocation.getLongitude()).isEqualTo(fuelStationDto.getLongitude());

    verify(userDataFacade).getUserData(anyLong());
    verify(fuelStationFacade).getAllFuelStations(anyDouble(), anyDouble(), anyDouble(), anyInt());
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = allInRadiusFuelStationCommand.getCommands();
    assertThat(commands).containsExactly(COMMAND);
  }
}
