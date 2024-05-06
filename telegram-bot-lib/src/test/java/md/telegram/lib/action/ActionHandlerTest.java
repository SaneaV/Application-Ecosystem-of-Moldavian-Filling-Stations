package md.telegram.lib.action;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
public class ActionHandlerTest {

  @Mock
  private DispatcherCommand dispatcherCommand;
  @Mock
  private DispatcherCallback dispatcherCallback;
  @InjectMocks
  private ActionHandler actionHandler;

  @Test
  @DisplayName("Should execute command")
  void shouldExecuteCommand() {
    final Update update = new Update();
    final Message message = new Message();
    update.setMessage(message);

    when(dispatcherCommand.getMessages(update)).thenReturn(emptyList());

    final List<? extends PartialBotApiMethod<?>> result = actionHandler.execute(update);

    verifyNoInteractions(dispatcherCallback);
    verify(dispatcherCommand).getMessages(update);
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Should execute callback")
  void shouldExecuteCallback() {
    final Update update = new Update();
    final CallbackQuery callback = new CallbackQuery();
    update.setCallbackQuery(callback);

    when(dispatcherCallback.getMessages(callback)).thenReturn(emptyList());

    final List<? extends PartialBotApiMethod<?>> result = actionHandler.execute(update);

    verifyNoInteractions(dispatcherCommand);
    verify(dispatcherCallback).getMessages(callback);
    assertThat(result).isEmpty();
  }
}
