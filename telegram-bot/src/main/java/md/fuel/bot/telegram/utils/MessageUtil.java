package md.fuel.bot.telegram.utils;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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

  public static SendLocation sendLocation(Long chatId, double latitude, double longitude, InlineKeyboardMarkup replyKeyboard) {
    return SendLocation.builder()
        .chatId(chatId)
        .latitude(latitude)
        .longitude(longitude)
        .replyMarkup(replyKeyboard)
        .build();
  }

  public static EditMessageText editMessageText(Long chatId, String text, Integer messageId, InlineKeyboardMarkup replyKeyboard) {
    return EditMessageText.builder()
        .text(text)
        .chatId(chatId)
        .messageId(messageId)
        .replyMarkup(replyKeyboard)
        .build();
  }

  public static DeleteMessage deleteMessage(long chatId, int messageId) {
    return DeleteMessage.builder()
        .chatId(chatId)
        .messageId(messageId)
        .build();
  }
}
