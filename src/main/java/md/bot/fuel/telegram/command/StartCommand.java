package md.bot.fuel.telegram.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.facade.UserDataFacade;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Collections.singletonList;
import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private static final String COMMAND = "/start";
    private static final String MESSAGE = "Welcome!\n" +
            "To start working with bot, you can select any element from the menu.\n\n" +
            "If you want to change the search radius, just send it to me (in kilometres, e.g. 0.5 (500 metres), 1 (1000 metres)" +
            ").\n\n" +
            "If you want to change your coordinates, just send your location.";

    private final UserDataFacade userDataFacade;

    @Override
    public List<? super PartialBotApiMethod<?>> execute(Update update) {
        final Long userId = update.getMessage().getFrom().getId();
        userDataFacade.addNewUser(userId);

        final SendMessage message = sendMessage(update, MESSAGE, getMainMenuKeyboard());
        return singletonList(message);
    }

    @Override
    public List<String> getCommands() {
        return singletonList(COMMAND);
    }
}
