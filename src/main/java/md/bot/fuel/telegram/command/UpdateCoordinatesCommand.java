package md.bot.fuel.telegram.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.facade.UserDataFacade;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

@Component
@RequiredArgsConstructor
public class UpdateCoordinatesCommand implements Command {

    private static final String MESSAGE = "New coordinates set!";

    private final UserDataFacade userDataFacade;

    @Override
    public List<? super PartialBotApiMethod<?>> execute(Update update) {
        final Message message = update.getMessage();
        final Long userId = message.getFrom().getId();
        final Location userLocation = message.getLocation();

        userDataFacade.updateCoordinates(userId, userLocation.getLatitude(), userLocation.getLongitude());

        final SendMessage sendMessage = sendMessage(update, MESSAGE, getMainMenuKeyboard());
        return singletonList(sendMessage);
    }

    @Override
    public List<String> getCommands() {
        return emptyList();
    }
}