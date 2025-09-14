//package md.fuel.bot.telegram.action;
//
//import static java.util.Collections.emptyList;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.anyDouble;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import md.fuel.bot.domain.FillingStation;
//import md.fuel.bot.facade.FillingStationFacade;
//import md.fuel.bot.facade.UserDataFacade;
//import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
//import md.fuel.bot.telegram.action.command.AllFillingStationInRadiusCommand;
//import md.fuel.bot.telegram.action.command.BestFuelInRadiusCommand;
//import md.fuel.bot.telegram.dto.UserDataDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//public class AllFillingStationInRadiusCommandTest {
//
//  private static final String COMMAND = "All filling stations";
//  private static final String FILLING_STATION_MESSAGE = """
//      ⛽ Filling station - "%s"
//
//      %s Petrol: %s lei
//      %s Diesel: %s lei
//      %s Gas: %s lei
//
//      📊 Last price update: %s""";
//  private static final String GREEN_CIRCLE = "🟢";
//  private static final String RED_CIRCLE = "🔴";
//  private static final long CHAT_ID = 20L;
//  private static final long USER_ID = 10L;
//
//  private final AllFillingStationInRadiusCommand allFillingStationInRadiusCommand;
//  private final FillingStationFacade fillingStationFacade;
//  private final UserDataFacade userDataFacade;
//
//  public AllFillingStationInRadiusCommandTest() {
//    this.fillingStationFacade = mock(FillingStationFacade.class);
//    this.userDataFacade = mock(UserDataFacade.class);
//    final ChatInfoHolder chatInfoHolder = new ChatInfoHolder();
//    chatInfoHolder.setChatInfo(USER_ID, CHAT_ID);
//
//    BestFuelInRadiusCommand.COMMAND = emptyList();
//
//    this.allFillingStationInRadiusCommand = new AllFillingStationInRadiusCommand(fillingStationFacade, userDataFacade,
//        chatInfoHolder);
//  }
//
//  @Test
//  @DisplayName("Should return all filling stations in radius message")
//  void shouldReturnAllFillingStationsInRadiusMessage() {
//    final String fuelStationName = "Filling Station Name";
//    final double petrol = 10;
//    final double diesel = 20;
//    final double latitude = 40;
//    final double longitude = 50;
//    final double defaultDoubleValue = 0;
//    final long defaultLongValue = 0L;
//    final Map<String, Double> priceMap = new LinkedHashMap<>();
//    priceMap.put("Petrol", petrol);
//    priceMap.put("Diesel", diesel);
//    priceMap.put("Gas", null);
//
//    final Update update = new Update();
//    final FillingStation fillingStation = new FillingStation(fuelStationName, priceMap, latitude, longitude);
//    FillingStation.timestamp = LocalDateTime.now().toString();
//    final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue, defaultDoubleValue);
//    final String messageText = String.format(FILLING_STATION_MESSAGE, fuelStationName, GREEN_CIRCLE, petrol, GREEN_CIRCLE, diesel,
//        RED_CIRCLE, 0.0, FillingStation.timestamp);
//
//    when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
//    when(fillingStationFacade.getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt())).thenReturn(fillingStation);
//
//    final List<? extends PartialBotApiMethod<?>> sendMessagesAndLocations = allFillingStationInRadiusCommand.execute(update);
//
//    assertThat(sendMessagesAndLocations).hasSize(1);
//    final SendMessage sendMessage = (SendMessage) sendMessagesAndLocations.get(0);
//    assertThat(sendMessage.getText()).isEqualTo(messageText);
//    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(CHAT_ID));
//
//    verify(userDataFacade).getUserData(anyLong());
//    verify(fillingStationFacade).getAllFillingStations(anyDouble(), anyDouble(), anyDouble(), anyInt());
//  }
//
//  @Test
//  @DisplayName("Should return command")
//  void shouldReturnCommand() {
//    final List<String> commands = allFillingStationInRadiusCommand.getCommands();
//    assertThat(commands).containsExactly(COMMAND);
//  }
//}
