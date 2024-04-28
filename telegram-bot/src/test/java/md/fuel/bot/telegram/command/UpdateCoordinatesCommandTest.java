package md.fuel.bot.telegram.command;

import static java.util.Collections.emptyList;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateCoordinatesCommandTest {

  private static final String MESSAGE = "New coordinates set!";
  private static final long CHAT_ID = 20L;
  private static final long USER_ID = 10L;

  private final UpdateCoordinatesCommand updateCoordinatesCommand;
  private final UserDataFacade userDataFacade;

  public UpdateCoordinatesCommandTest() {
    this.userDataFacade = mock(UserDataFacade.class);
    final ChatInfoHolder chatInfoHolder = new ChatInfoHolder();
    chatInfoHolder.setChatInfo(USER_ID, CHAT_ID);

    BestFuelInRadiusCommand.COMMAND = emptyList();

    this.updateCoordinatesCommand = new UpdateCoordinatesCommand(userDataFacade, chatInfoHolder);
  }

  @Test
  @DisplayName("Should return coordinates updated message")
  void shouldReturnCoordinatesUpdatedMessage() {
    final double coordinates = 10.0;

    final Update update = new Update();
    final Message message = new Message();
    final Location location = new Location();

    location.setLatitude(coordinates);
    location.setLongitude(coordinates);
    message.setLocation(location);
    update.setMessage(message);

    final List<SendMessage> messages = updateCoordinatesCommand.execute(update).stream()
        .map(m -> (SendMessage) m)
        .toList();

    assertThat(messages).hasSize(1);
    final SendMessage sendMessage = messages.get(0);
    assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(CHAT_ID));
    assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());

    verify(userDataFacade).updateCoordinates(USER_ID, coordinates, coordinates);
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = updateCoordinatesCommand.getCommands();
    assertThat(commands).isEmpty();
  }
}
