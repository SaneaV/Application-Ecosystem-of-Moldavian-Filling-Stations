package md.fuel.bot.telegram.command;

import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getFuelTypeKeyboard;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class SpecificFuelInRadiusCommandTest {

  private static final String COMMAND = "Best fuel price";
  private static final String MESSAGE = "Select the desired type of fuel.";

  private final SpecificFuelInRadiusCommand specificFuelInRadiusCommand;

  public SpecificFuelInRadiusCommandTest() {
    this.specificFuelInRadiusCommand = new SpecificFuelInRadiusCommand();
  }

  @Test
  @DisplayName("Should return specific fuel in radius message")
  void shouldReturnSpecificFuelInRadiusMessage() {
    final long chatId = 20L;
    final long userId = 20L;

    final Update update = new Update();
    final Message message = new Message();
    final User from = new User();
    final Chat chat = new Chat();

    chat.setId(chatId);
    from.setId(userId);
    message.setChat(chat);
    message.setFrom(from);
    update.setMessage(message);

    final List<SendMessage> messages = specificFuelInRadiusCommand.execute(update).stream()
        .map(m -> (SendMessage) m)
        .toList();

    assertThat(messages).hasSize(1);
    final SendMessage sendMessage = messages.get(0);
    assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(chatId));
    assertThat(sendMessage.getReplyMarkup()).isEqualTo(getFuelTypeKeyboard());
  }

  @Test
  @DisplayName("Should return command")
  void shouldReturnCommand() {
    final List<String> commands = specificFuelInRadiusCommand.getCommands();
    assertThat(commands).containsExactly(COMMAND);
  }
}
