package md.fuel.bot.telegram.command;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static wiremock.com.github.jknack.handlebars.internal.lang3.StringUtils.EMPTY;

import java.util.List;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DispatcherCommandTest {

  private static final String COMMAND_NOT_FOUND = "I don't understand your command, use the menu buttons.";

  private final DispatcherCommand dispatcherCommand;
  private final UpdateRadiusCommand updateRadiusCommand;
  private final UpdateCoordinatesCommand updateCoordinatesCommand;
  private final Command command;

  public DispatcherCommandTest() {
    this.updateRadiusCommand = mock(UpdateRadiusCommand.class);
    this.updateCoordinatesCommand = mock(UpdateCoordinatesCommand.class);
    this.command = mock(Command.class);
    this.dispatcherCommand = new DispatcherCommand(singletonList(command), updateRadiusCommand, updateCoordinatesCommand);
  }

  @Test
  @DisplayName("Should return message on update coordinates")
  void shouldReturnMessageOnUpdateCoordinates() {
    final Update update = mock(Update.class);
    final Message message = mock(Message.class);
    final SendMessage sendMessage = mock(SendMessage.class);

    when(update.getMessage()).thenReturn(message);
    when(message.hasLocation()).thenReturn(true);
    when(updateCoordinatesCommand.execute(update)).thenReturn(singletonList(sendMessage));

    final List<? super BotApiMethod<?>> messages = dispatcherCommand.getMessages(update);

    verify(update, times(2)).getMessage();
    verify(message).hasLocation();
    verify(updateCoordinatesCommand).execute(any());

    assertThat(messages).hasSize(1);
    assertThat(messages.get(0)).isEqualTo(sendMessage);
  }

  @Test
  @DisplayName("Should return message on update radius")
  void shouldReturnMessageOnUpdateRadius() {
    final Update update = mock(Update.class);
    final Message message = mock(Message.class);
    final SendMessage sendMessage = mock(SendMessage.class);

    when(update.getMessage()).thenReturn(message);
    when(message.hasLocation()).thenReturn(false);
    when(message.getText()).thenReturn("10.0");
    when(updateRadiusCommand.execute(update)).thenReturn(singletonList(sendMessage));

    final List<? super BotApiMethod<?>> messages = dispatcherCommand.getMessages(update);

    verify(update, times(2)).getMessage();
    verify(message).hasLocation();
    verify(updateRadiusCommand).execute(any());

    assertThat(messages).hasSize(1);
    assertThat(messages.get(0)).isEqualTo(sendMessage);
  }

  @Test
  @DisplayName("Should return message on existing command")
  void shouldReturnMessageOnExistingCommand() {
    final String textCommand = "command";
    final Update update = mock(Update.class);
    final Message message = mock(Message.class);
    final SendMessage sendMessage = mock(SendMessage.class);

    when(update.getMessage()).thenReturn(message);
    when(message.hasLocation()).thenReturn(false);
    when(message.getText()).thenReturn(textCommand);
    when(command.getCommands()).thenReturn(singletonList(textCommand));
    when(command.execute(any())).thenReturn(singletonList(sendMessage));

    final List<? super BotApiMethod<?>> messages = dispatcherCommand.getMessages(update);

    verify(update, times(2)).getMessage();
    verify(message).hasLocation();
    verify(command, times(2)).getCommands();
    verify(command).execute(any());

    assertThat(messages).hasSize(1);
    assertThat(messages.get(0)).isEqualTo(sendMessage);
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on non-existing command")
  void shouldThrowEntityNotFoundExceptionOnNonExistingCommand() {
    final Update update = mock(Update.class);
    final Message message = mock(Message.class);

    when(update.getMessage()).thenReturn(message);
    when(message.hasLocation()).thenReturn(false);
    when(command.getCommands()).thenReturn(singletonList("textCommand"));
    when(message.getText()).thenReturn("command");

    assertThatThrownBy(() -> dispatcherCommand.getMessages(update))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(COMMAND_NOT_FOUND);

    verify(update, times(2)).getMessage();
    verify(message).hasLocation();
    verify(command, times(2)).getCommands();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on empty text")
  void shouldThrowEntityNotFoundExceptionOnNullCommand() {
    final Update update = mock(Update.class);
    final Message message = mock(Message.class);

    when(update.getMessage()).thenReturn(message);
    when(message.getText()).thenReturn(EMPTY);

    assertThatThrownBy(() -> dispatcherCommand.getMessages(update))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(COMMAND_NOT_FOUND);

    verify(update, times(2)).getMessage();
    verify(message).hasLocation();
    verify(command, times(1)).getCommands();
  }

  @Test
  @DisplayName("Should throw EntityNotFoundException on empty list of commands")
  void shouldThrowEntityNotFoundExceptionOnEmptyListOfCommands() {
    final Update update = mock(Update.class);
    final Message message = mock(Message.class);

    when(update.getMessage()).thenReturn(message);
    when(message.getText()).thenReturn("command");
    when(command.getCommands()).thenReturn(emptyList());

    assertThatThrownBy(() -> dispatcherCommand.getMessages(update))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage(COMMAND_NOT_FOUND);

    verify(update, times(2)).getMessage();
    verify(message).hasLocation();
    verify(command).getCommands();
  }
}
