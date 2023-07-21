package md.bot.fuel.telegram.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.domain.UserData;
import md.bot.fuel.facade.FuelStationFacade;
import md.bot.fuel.facade.UserDataFacade;
import md.bot.fuel.facade.dto.FuelStationDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static md.bot.fuel.telegram.converter.MessageConverter.toMessage;
import static md.bot.fuel.telegram.utils.MessageUtil.sendLocation;
import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

@Component
@RequiredArgsConstructor
public class NearestFuelStationCommand implements Command {

    public final static String COMMAND = "Nearest fuel station";

    private final FuelStationFacade fuelStationFacade;
    private final UserDataFacade userDataFacade;

    @Override
    public List<? super PartialBotApiMethod<?>> execute(Update update) {
        final long userId = update.getMessage().getFrom().getId();
        final long chatId = update.getMessage().getChatId();
        final UserData userData = userDataFacade.getUserData(userId);
        final FuelStationDto nearestFuelStation = fuelStationFacade.getNearestFuelStation(userData.getLatitude(),
                userData.getLongitude(), userData.getRadius());

        final String fuelStationTextMessage = toMessage(nearestFuelStation);
        final SendMessage fuelStationMessage = sendMessage(chatId, fuelStationTextMessage, getMainMenuKeyboard());
        final SendLocation fuelStationLocation = sendLocation(chatId, nearestFuelStation.getLatitude(),
                nearestFuelStation.getLongitude());

        return asList(fuelStationMessage, fuelStationLocation);
    }

    @Override
    public List<String> getCommands() {
        return singletonList(COMMAND);
    }
}
