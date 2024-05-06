package md.fuel.bot.telegram.action.callback;

import java.util.List;
import lombok.RequiredArgsConstructor;
import md.telegram.lib.action.Callback;
import md.telegram.lib.action.DispatcherCallback;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class DispatcherCallbackImpl implements DispatcherCallback {

  private final List<Callback> callbackList;
  private final CallbackHolder callbackHolder;

  @Override
  public List<? extends PartialBotApiMethod<?>> getMessages(CallbackQuery callbackQuery) {
    callbackHolder.parse(callbackQuery.getData(), callbackQuery.getMessage().getMessageId());
    final String callbackType = callbackHolder.getCallbackType();

    return callbackList.stream()
        .filter(type -> type.getCallbackType().equals(callbackType))
        .findFirst()
        .orElseThrow()
        .execute(callbackQuery);
  }
}