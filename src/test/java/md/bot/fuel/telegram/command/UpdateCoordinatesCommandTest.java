package md.bot.fuel.telegram.command;

import java.util.List;
import md.bot.fuel.facade.UserDataFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static java.util.stream.Collectors.toList;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateCoordinatesCommandTest {

    private static final String MESSAGE = "New coordinates set!";

    private final UpdateCoordinatesCommand updateCoordinatesCommand;
    private final UserDataFacade userDataFacade;

    public UpdateCoordinatesCommandTest() {
        this.userDataFacade = mock(UserDataFacade.class);
        this.updateCoordinatesCommand = new UpdateCoordinatesCommand(userDataFacade);
    }

    @Test
    @DisplayName("Should return coordinates updated message")
    void shouldReturnCoordinatesUpdatedMessage() {
        final long userId = 10L;
        final long chatId = 20L;
        final double coordinates = 10.0;

        final Update update = new Update();
        final Message message = new Message();
        final User user = new User();
        final Chat chat = new Chat();
        final Location location = new Location();

        location.setLatitude(coordinates);
        location.setLongitude(coordinates);
        user.setId(userId);
        chat.setId(chatId);
        message.setFrom(user);
        message.setChat(chat);
        message.setLocation(location);
        update.setMessage(message);

        final List<SendMessage> messages = updateCoordinatesCommand.execute(update).stream()
                .map(m -> (SendMessage) m)
                .collect(toList());

        assertThat(messages).hasSize(1);
        final SendMessage sendMessage = messages.get(0);
        assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
        assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
        assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());

        verify(userDataFacade).updateCoordinates(userId, coordinates, coordinates);
    }

    @Test
    @DisplayName("Should return command")
    void shouldReturnCommand() {
        final List<String> commands = updateCoordinatesCommand.getCommands();
        assertThat(commands).isEmpty();
    }
}
