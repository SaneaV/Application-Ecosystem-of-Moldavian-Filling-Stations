package md.fuel.bot.telegram.action;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ActionHandler {

  private final DispatcherCommand dispatcherCommand;
  private final DispatcherCallback dispatcherCallback;

  public List<? super BotApiMethod<?>> execute(Update update) {
    if (update.hasCallbackQuery()) {
      return dispatcherCallback.getMessages(update.getCallbackQuery());
    }
    return dispatcherCommand.getMessages(update);
  }
}