package md.fuel.bot.telegram.command;

import static java.util.Collections.emptyList;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getFuelTypeKeyboard;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SpecificFuelInRadiusCommandTest {

  private static final String COMMAND = "Best fuel price";
  private static final String MESSAGE = "Select the desired type of fuel.";
  private static final long CHAT_ID = 20L;
  private static final long USER_ID = 10L;

  private final SpecificFuelInRadiusCommand specificFuelInRadiusCommand;

  public SpecificFuelInRadiusCommandTest() {
    final ChatInfoHolder chatInfoHolder = new ChatInfoHolder();
    chatInfoHolder.setChatInfo(USER_ID, CHAT_ID);

    BestFuelInRadiusCommand.COMMAND = emptyList();

    this.specificFuelInRadiusCommand = new SpecificFuelInRadiusCommand(chatInfoHolder);
  }

  @Test
  @DisplayName("Should return specific fuel in radius message")
  void shouldReturnSpecificFuelInRadiusMessage() {
    final Update update = new Update();

    final List<SendMessage> messages = specificFuelInRadiusCommand.execute(update).stream()
        .map(m -> (SendMessage) m)
        .toList();

    assertThat(messages).hasSize(1);
    final SendMessage sendMessage = messages.get(0);
    assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(CHAT_ID));
    assertThat(sendMessage.getReplyMarkup()).isEqualTo(getFuelTypeKeyboard());
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = specificFuelInRadiusCommand.getCommands();
    assertThat(commands).containsExactly(COMMAND);
  }
}
