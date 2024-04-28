package md.fuel.bot.infrastructure.configuration;

import org.springframework.stereotype.Component;

@Component
public class ChatInfoHolder {

  public static final InheritableThreadLocal<Long> USER_ID = new InheritableThreadLocal<>();
  public static final InheritableThreadLocal<Long> CHAT_ID = new InheritableThreadLocal<>();

  public void setChatInfo(long userId, long chatId) {
    USER_ID.set(userId);
    CHAT_ID.set(chatId);
  }

  public long getUserId() {
    return USER_ID.get();
  }

  public long getChatId() {
    return CHAT_ID.get();
  }
}
