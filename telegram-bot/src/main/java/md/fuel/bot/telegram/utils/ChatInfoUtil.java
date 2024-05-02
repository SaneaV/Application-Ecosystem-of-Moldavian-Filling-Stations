package md.fuel.bot.telegram.utils;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ChatInfoUtil {

  private final ChatInfoHolder chatInfoHolder;

  public void setChatInfo(Update update) {
    final long userId;
    final Long chatId;

    if (update.hasCallbackQuery()) {
      final CallbackQuery callbackQuery = update.getCallbackQuery();
      userId = callbackQuery.getFrom().getId();
      chatId = callbackQuery.getMessage().getChatId();
    } else {
      final Message message = update.getMessage();
      userId = message.getFrom().getId();
      chatId = message.getChatId();
    }

    chatInfoHolder.setChatInfo(userId, chatId);
  }
}
