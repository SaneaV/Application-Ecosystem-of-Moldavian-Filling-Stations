package md.bot.fuel.telegram.command;

import java.util.List;
import md.bot.fuel.facade.UserDataFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static java.util.stream.Collectors.toList;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StartCommandTest {

    private static final String COMMAND = "/start";
    private static final String MESSAGE = "Welcome!\n" +
            "To start working with bot, you can select any element from the menu.\n\n" +
            "If you want to change the search radius, just send it to me (in kilometres, e.g. 0.5 (500 metres), 1 (1000 metres)).\n\n" +
            "If you want to change your coordinates, just send your location.";

    private final UserDataFacade userDataFacade;
    private final StartCommand startCommand;

    public StartCommandTest() {
        this.userDataFacade = mock(UserDataFacade.class);
        this.startCommand = new StartCommand(userDataFacade);
    }

    @Test
    @DisplayName("Should return welcome message")
    void shouldReturnWelcomeMessage() {
        final long userId = 10L;
        final long chatId = 20L;

        final Update update = new Update();
        final Message message = new Message();
        final User user = new User();
        final Chat chat = new Chat();

        user.setId(userId);
        chat.setId(chatId);
        message.setFrom(user);
        message.setChat(chat);
        update.setMessage(message);

        final List<SendMessage> messages = startCommand.execute(update).stream()
                .map(m -> (SendMessage) m)
                .collect(toList());

        assertThat(messages).hasSize(1);
        final SendMessage sendMessage = messages.get(0);
        assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
        assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
        assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());

        verify(userDataFacade).addNewUser(userId);
    }

    @Test
    @DisplayName("Should return command")
    void shouldReturnCommand() {
        final List<String> commands = startCommand.getCommands();
        assertThat(commands).containsExactly(COMMAND);
    }
}
