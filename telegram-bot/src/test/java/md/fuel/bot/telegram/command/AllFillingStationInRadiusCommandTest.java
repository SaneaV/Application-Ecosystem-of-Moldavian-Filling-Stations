package md.fuel.bot.telegram.command;

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
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.telegram.dto.UserDataDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class AllFillingStationInRadiusCommandTest {

  private static final String COMMAND = "All filling stations";
  private static final String FILLING_STATION_MESSAGE = "⛽ Filling station - \"%s\"\n\n"
      + "%s Petrol: %s lei\n"
      + "%s Diesel: %s lei\n"
      + "%s Gas : %s lei\n\n"
      + "📊 Last price update: %s";
  private static final String GREEN_CIRCLE = "🟢";
  private static final String RED_CIRCLE = "🔴";

  private final AllFillingStationInRadiusCommand allFillingStationInRadiusCommand;
  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;

  public AllFillingStationInRadiusCommandTest() {
    this.fillingStationFacade = mock(FillingStationFacade.class);
    this.userDataFacade = mock(UserDataFacade.class);
    this.allFillingStationInRadiusCommand = new AllFillingStationInRadiusCommand(fillingStationFacade, userDataFacade);
  }

  @Test
  @DisplayName("Should return all filling stations in radius message")
  void shouldReturnAllFillingStationsInRadiusMessage() {
    final long chatId = 20L;
    final long userId = 10L;
    final String fuelStationName = "Filling Station Name";
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
    final FillingStation fillingStation = new FillingStation(fuelStationName, petrol, diesel, gas, latitude, longitude);
    FillingStation.timestamp = LocalDateTime.now().toString();
    final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue, defaultDoubleValue);
    final String messageText = String.format(FILLING_STATION_MESSAGE, fuelStationName, GREEN_CIRCLE, petrol, GREEN_CIRCLE, diesel,
        RED_CIRCLE, 0.0, FillingStation.timestamp);

    user.setId(userId);
    chat.setId(chatId);
    message.setFrom(user);
    message.setChat(chat);
    update.setMessage(message);

    when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
    when(fillingStationFacade.getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt())).thenReturn(
        singletonList(fillingStation));

    final List<? super PartialBotApiMethod<?>> sendMessagesAndLocations = allFillingStationInRadiusCommand.execute(update);

    assertThat(sendMessagesAndLocations).hasSize(2);
    final SendMessage sendMessage = (SendMessage) sendMessagesAndLocations.get(0);
    final SendLocation sendLocation = (SendLocation) sendMessagesAndLocations.get(1);
    assertThat(sendMessage.getText()).isEqualTo(messageText);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendLocation.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendLocation.getLatitude()).isEqualTo(fillingStation.getLatitude());
    assertThat(sendLocation.getLongitude()).isEqualTo(fillingStation.getLongitude());

    verify(userDataFacade).getUserData(anyLong());
    verify(fillingStationFacade).getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt());
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = allFillingStationInRadiusCommand.getCommands();
    assertThat(commands).containsExactly(COMMAND);
  }
}
