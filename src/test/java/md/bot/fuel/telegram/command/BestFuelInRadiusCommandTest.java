package md.bot.fuel.telegram.command;

import java.time.LocalDateTime;
import java.util.List;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.UserDataFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import md.bot.fuel.facade.dto.UserDataDto;
import md.bot.fuel.infrastructure.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BestFuelInRadiusCommandTest {

    public static final String PETROL = "Petrol";
    public static final String DIESEL = "Diesel";
    public static final String GAS = "Gas";
    private static final String SPECIFIC_FUEL_STATION_MESSAGE = "⛽ Fuel station - \"%s\"\n\n" +
            "%s %s: %s lei\n\n" +
            "\uD83D\uDCCA Last price update: %s";
    private static final String GREEN_CIRCLE = "\uD83D\uDFE2";
    private static final String ERROR_NO_FUEL_TYPE_EXIST = "Can't find specified fuel type";

    private final BestFuelInRadiusCommand bestFuelInRadiusCommand;
    private final FuelStationFacade fuelStationFacade;
    private final UserDataFacade userDataFacade;

    public BestFuelInRadiusCommandTest() {
        this.fuelStationFacade = mock(FuelStationFacade.class);
        this.userDataFacade = mock(UserDataFacade.class);
        this.bestFuelInRadiusCommand = new BestFuelInRadiusCommand(fuelStationFacade, userDataFacade);
    }

    @ParameterizedTest
    @ValueSource(strings = {PETROL, DIESEL, GAS})
    @DisplayName("Should return best fuel price in radius message")
    void shouldReturnBestFuelPriceInRadiusMessage(String fuelType) {
        final long chatId = 20L;
        final long userId = 10L;
        final String fuelStationName = "Fuel Station Name";
        final double price = 10;
        final double latitude = 40;
        final double longitude = 50;
        final double defaultDoubleValue = 0;
        final long defaultLongValue = 0L;

        final User user = new User();
        final Update update = new Update();
        final Message message = new Message();
        final Chat chat = new Chat();
        final FuelStationDto fuelStationDto = new FuelStationDto(fuelStationName, price, price, price, latitude, longitude);
        FuelStationDto.timestamp = LocalDateTime.now().toString();
        final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue,
                defaultDoubleValue);
        final String messageText = String.format(SPECIFIC_FUEL_STATION_MESSAGE, fuelStationName, GREEN_CIRCLE, fuelType, price,
                FuelStationDto.timestamp);

        user.setId(userId);
        chat.setId(chatId);
        message.setFrom(user);
        message.setText(fuelType);
        message.setChat(chat);
        update.setMessage(message);

        when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
        when(fuelStationFacade.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt())).thenReturn(singletonList(fuelStationDto));

        final List<? super PartialBotApiMethod<?>> sendMessagesAndLocations = bestFuelInRadiusCommand.execute(update);

        assertThat(sendMessagesAndLocations).hasSize(2);
        final SendMessage sendMessage = (SendMessage) sendMessagesAndLocations.get(0);
        final SendLocation sendLocation = (SendLocation) sendMessagesAndLocations.get(1);
        assertThat(sendMessage.getText()).isEqualTo(messageText);
        assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
        assertThat(sendLocation.getChatId()).isEqualTo(Long.toString(chatId));
        assertThat(sendLocation.getLatitude()).isEqualTo(fuelStationDto.getLatitude());
        assertThat(sendLocation.getLongitude()).isEqualTo(fuelStationDto.getLongitude());

        verify(userDataFacade).getUserData(anyLong());
        verify(fuelStationFacade).getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if fuelType not found")
    void shouldThrowEntityNotFoundExceptionIfFuelTypeNotFound() {
        final long chatId = 20L;
        final long userId = 10L;
        final String fuelStationName = "Fuel Station Name";
        final double price = 10;
        final double latitude = 40;
        final double longitude = 50;
        final double defaultDoubleValue = 0;
        final long defaultLongValue = 0L;

        final User user = new User();
        final Update update = new Update();
        final Message message = new Message();
        final Chat chat = new Chat();
        final FuelStationDto fuelStationDto = new FuelStationDto(fuelStationName, price, price, price, latitude, longitude);
        FuelStationDto.timestamp = LocalDateTime.now().toString();
        final UserDataDto userDataDto = new UserDataDto(defaultLongValue, defaultDoubleValue, defaultDoubleValue,
                defaultDoubleValue);

        user.setId(userId);
        chat.setId(chatId);
        message.setFrom(user);
        message.setText("Invalid Fuel Type");
        message.setChat(chat);
        update.setMessage(message);

        when(userDataFacade.getUserData(anyLong())).thenReturn(userDataDto);
        when(fuelStationFacade.getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt())).thenReturn(singletonList(fuelStationDto));

        assertThatThrownBy(() ->  bestFuelInRadiusCommand.execute(update))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ERROR_NO_FUEL_TYPE_EXIST);

        verify(userDataFacade).getUserData(anyLong());
        verify(fuelStationFacade).getBestFuelPrice(anyDouble(), anyDouble(), anyDouble(), anyString(), anyInt());
    }

    @Test
    @DisplayName("Should return commands")
    void shouldReturnCommands() {
        final List<String> commands = bestFuelInRadiusCommand.getCommands();
        assertThat(commands).containsExactly(PETROL, DIESEL, GAS);
    }
}
