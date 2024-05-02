package md.fuel.bot.telegram.action.callback;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.bot.telegram.action.DispatcherCallback;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class DispatcherCallbackImpl implements DispatcherCallback {

  private final List<Callback> callbackList;
  private final CallbackHolder callbackHolder;

  @Override
  public List<? super BotApiMethod<?>> getMessages(CallbackQuery callbackQuery) {
    callbackHolder.parse(callbackQuery.getData(), callbackQuery.getMessage().getMessageId());
    final String callbackType = callbackHolder.getCallbackType();

    return callbackList.stream()
        .filter(type -> type.getCallbackType().equals(callbackType))
        .findFirst()
        .orElseThrow()
        .execute(callbackQuery);
  }
}
