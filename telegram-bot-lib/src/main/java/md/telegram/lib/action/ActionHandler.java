package md.telegram.lib.action;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ActionHandler {

  private final DispatcherCommand dispatcherCommand;
  private final DispatcherCallback dispatcherCallback;

  public ActionHandler(DispatcherCommand dispatcherCommand,
      @Autowired(required = false) DispatcherCallback dispatcherCallback) {
    this.dispatcherCommand = dispatcherCommand;
    this.dispatcherCallback = dispatcherCallback;
  }

  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    if (update.hasCallbackQuery()) {
      return dispatcherCallback.getMessages(update.getCallbackQuery());
    }
    return dispatcherCommand.getMessages(update);
  }
}