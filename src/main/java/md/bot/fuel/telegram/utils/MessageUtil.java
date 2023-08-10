package md.bot.fuel.telegram.utils;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor(access = PRIVATE)
public class MessageUtil {

  public static SendMessage sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
    return SendMessage.builder()
        .chatId(chatId)
        .text(text)
        .replyMarkup(replyKeyboard)
        .build();
  }

  public static SendMessage sendMessage(Update update, String text, ReplyKeyboard replyKeyboard) {
    return SendMessage.builder()
        .chatId(getChatId(update))
        .text(text)
        .replyMarkup(replyKeyboard)
        .build();
  }

  public static SendMessage sendMessage(Long chatId, String text) {
    return SendMessage.builder()
        .chatId(chatId)
        .text(text)
        .build();
  }

  public static SendLocation sendLocation(Long chatId, double latitude, double longitude) {
    return SendLocation.builder()
        .chatId(chatId)
        .latitude(latitude)
        .longitude(longitude)
        .build();
  }

  private static Long getChatId(Update update) {
    return update.getMessage().getChatId();
  }
}
