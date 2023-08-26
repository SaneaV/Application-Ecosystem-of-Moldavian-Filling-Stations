package md.fuel.bot.telegram.command;

import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

public class NearestFillingStationCommandTest {

  public static final String COMMAND = "Nearest filling station";
  private static final String FILLING_STATION_MESSAGE = """
      ⛽ Filling station - "%s"

      %s Petrol: %s lei
      %s Diesel: %s lei
      %s Gas: %s lei

      📊 Last price update: %s""";
  private static final String GREEN_CIRCLE = "🟢";
  private static final String RED_CIRCLE = "🔴";

  private final NearestFillingStationCommand nearestFillingStationCommand;
  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;

  public NearestFillingStationCommandTest() {
    this.fillingStationFacade = mock(FillingStationFacade.class);
    this.userDataFacade = mock(UserDataFacade.class);
    this.nearestFillingStationCommand = new NearestFillingStationCommand(fillingStationFacade, userDataFacade);
  }

  @Test
  @DisplayName("Should return nearest filling station in radius message")
  void shouldReturnNearestFillingStationInRadiusMessage() {
    final long chatId = 20L;
    final long userId = 10L;
    final String fuelStationName = "Filling Station Name";
    final double petrol = 10;
    final double gas = 30;
    final double latitude = 40;
    final double longitude = 50;
    final double defaultDoubleValue = 0;
    final long defaultLongValue = 0L;
    final Map<String, Double> priceMap = new LinkedHashMap<>();
    priceMap.put("Petrol", petrol);
    priceMap.put("Diesel", null);
    priceMap.put("Gas", gas);

    final User user = new User();
    final Update update = new Update();
    final Message message = new Message();
    final Chat chat = new Chat();
    final FillingStation fillingStationDto = new FillingStation(fuelStationName, priceMap, latitude, longitude);
    FillingStation.timestamp = LocalDateTime.now().toString();
    final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue, defaultDoubleValue);
    final String messageText = String.format(FILLING_STATION_MESSAGE, fuelStationName, GREEN_CIRCLE, petrol, RED_CIRCLE, 0.0,
        GREEN_CIRCLE, gas, FillingStation.timestamp);

    user.setId(userId);
    chat.setId(chatId);
    message.setFrom(user);
    message.setChat(chat);
    update.setMessage(message);

    when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
    when(fillingStationFacade.getNearestFillingStation(anyDouble(), anyDouble(), anyDouble())).thenReturn(fillingStationDto);

    final List<? super PartialBotApiMethod<?>> sendMessagesAndLocations = nearestFillingStationCommand.execute(update);

    assertThat(sendMessagesAndLocations).hasSize(2);
    final SendMessage sendMessage = (SendMessage) sendMessagesAndLocations.get(0);
    final SendLocation sendLocation = (SendLocation) sendMessagesAndLocations.get(1);
    assertThat(sendMessage.getText()).isEqualTo(messageText);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());
    assertThat(sendLocation.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendLocation.getLatitude()).isEqualTo(fillingStationDto.latitude());
    assertThat(sendLocation.getLongitude()).isEqualTo(fillingStationDto.longitude());

    verify(userDataFacade).getUserData(anyLong());
    verify(fillingStationFacade).getNearestFillingStation(anyDouble(), anyDouble(), anyDouble());
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = nearestFillingStationCommand.getCommands();
    assertThat(commands).containsExactly(COMMAND);
  }
}
