package md.fuel.bot.telegram.action;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.domain.FuelType;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.action.command.BestFuelInRadiusCommand;
import md.fuel.bot.telegram.dto.UserDataDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BestFuelInRadiusCommandTest {

  public static final String PETROL = "Petrol";
  public static final String DIESEL = "Diesel";
  public static final String GAS = "Gas";
  private static final String SPECIFIC_FILLING_STATION_MESSAGE = """
      ⛽ Filling station - "%s"

      %s %s: %s lei

      📊 Last price update: %s""";
  private static final String GREEN_CIRCLE = "🟢";
  private static final long CHAT_ID = 20L;
  private static final long USER_ID = 10L;

  private final BestFuelInRadiusCommand bestFuelInRadiusCommand;
  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;

  public BestFuelInRadiusCommandTest() {
    this.fillingStationFacade = mock(FillingStationFacade.class);
    this.userDataFacade = mock(UserDataFacade.class);
    final ChatInfoHolder chatInfoHolder = new ChatInfoHolder();
    chatInfoHolder.setChatInfo(USER_ID, CHAT_ID);

    final FuelType fuelType = new FuelType(asList(PETROL, DIESEL, GAS));
    when(fillingStationFacade.getSupportedFuelTypes()).thenReturn(fuelType);

    this.bestFuelInRadiusCommand = new BestFuelInRadiusCommand(fillingStationFacade, userDataFacade, chatInfoHolder);
  }

  @ParameterizedTest
  @ValueSource(strings = {PETROL, DIESEL, GAS})
  @DisplayName("Should return best fuel price in radius message")
  void shouldReturnBestFuelPriceInRadiusMessage(String fuelType) {
    final String fuelStationName = "Filling Station Name";
    final double price = 10;
    final double latitude = 40;
    final double longitude = 50;
    final double defaultDoubleValue = 0;
    final long defaultLongValue = 0L;
    final Map<String, Double> priceMap = Map.of("Petrol", price, "Diesel", price, "Gas", price);

    final Update update = new Update();
    final Message message = new Message();
    final FillingStation fillingStation = new FillingStation(fuelStationName, priceMap, latitude, longitude);
    FillingStation.timestamp = LocalDateTime.now().toString();
    final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue, defaultDoubleValue);
    final String messageText = String.format(SPECIFIC_FILLING_STATION_MESSAGE, fuelStationName, GREEN_CIRCLE, fuelType, price,
        FillingStation.timestamp);

    message.setText(fuelType);
    update.setMessage(message);

    when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
    when(fillingStationFacade.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt())).thenReturn(
        fillingStation);

    final List<? extends PartialBotApiMethod<?>> sendMessagesAndLocations = bestFuelInRadiusCommand.execute(update);

    assertThat(sendMessagesAndLocations).hasSize(2);
    final SendMessage sendMessage = (SendMessage) sendMessagesAndLocations.get(0);
    assertThat(sendMessage.getText()).isEqualTo(messageText);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(CHAT_ID));

    verify(userDataFacade).getUserData(anyLong());
    verify(fillingStationFacade).getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt());
  }

  @Test
  @DisplayName("Should return commands")
  void shouldReturnCommands() {
    final List<String> commands = bestFuelInRadiusCommand.getCommands();
    assertThat(commands).containsExactly(PETROL, DIESEL, GAS);
  }
}
