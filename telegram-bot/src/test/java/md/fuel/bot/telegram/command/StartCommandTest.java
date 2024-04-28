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
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommandTest {

  private static final String COMMAND = "/start";
  private static final String MESSAGE = """
      Welcome!
      To start working with bot, you can select any element from the menu.

      If you want to change the search radius, just send it to me (in kilometres, e.g. 0.5 (500 metres), 1 (1000 metres)).

      If you want to change your coordinates, just send your location.""";
  private static final long CHAT_ID = 20L;
  private static final long USER_ID = 10L;

  private final UserDataFacade userDataFacade;
  private final StartCommand startCommand;

  public StartCommandTest() {
    BestFuelInRadiusCommand.COMMAND = emptyList();

    this.userDataFacade = mock(UserDataFacade.class);
    final ChatInfoHolder chatInfoHolder = new ChatInfoHolder();
    chatInfoHolder.setChatInfo(USER_ID, CHAT_ID);

    this.startCommand = new StartCommand(userDataFacade, chatInfoHolder);
  }

  @Test
  @DisplayName("Should return welcome message")
  void shouldReturnWelcomeMessage() {
    final Update update = new Update();

    final List<SendMessage> messages = startCommand.execute(update).stream()
        .map(m -> (SendMessage) m)
        .toList();

    assertThat(messages).hasSize(1);
    final SendMessage sendMessage = messages.get(0);
    assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(CHAT_ID));
    assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());

    verify(userDataFacade).addNewUser(USER_ID);
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = startCommand.getCommands();
    assertThat(commands).containsExactly(COMMAND);
  }
}
