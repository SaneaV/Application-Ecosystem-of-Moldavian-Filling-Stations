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

public class UpdateRadiusCommandTest {

    private static final String MESSAGE = "New radius set!";

    private final UpdateRadiusCommand updateRadiusCommand;
    private final UserDataFacade userDataFacade;

    public UpdateRadiusCommandTest() {
        this.userDataFacade = mock(UserDataFacade.class);
        this.updateRadiusCommand = new UpdateRadiusCommand(userDataFacade);
    }

    @Test
    @DisplayName("Should return radius updated message")
    void shouldReturnRadiusUpdatedMessage() {
        final long userId = 10L;
        final long chatId = 20L;
        final double radius = 10.0;

        final Update update = new Update();
        final Message message = new Message();
        final User user = new User();
        final Chat chat = new Chat();

        user.setId(userId);
        chat.setId(chatId);
        message.setFrom(user);
        message.setChat(chat);
        message.setText(Double.toString(radius));
        update.setMessage(message);

        final List<SendMessage> messages = updateRadiusCommand.execute(update).stream()
                .map(m -> (SendMessage) m)
                .collect(toList());

        assertThat(messages).hasSize(1);
        final SendMessage sendMessage = messages.get(0);
        assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
        assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
        assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());

        verify(userDataFacade).updateRadius(userId, radius);
    }

    @Test
    @DisplayName("Should return command")
    void shouldReturnCommand() {
        final List<String> commands = updateRadiusCommand.getCommands();
        assertThat(commands).isEmpty();
    }
}
