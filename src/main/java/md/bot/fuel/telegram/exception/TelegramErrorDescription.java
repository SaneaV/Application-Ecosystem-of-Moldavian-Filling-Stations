package md.bot.fuel.telegram.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Getter
@AllArgsConstructor
public class TelegramErrorDescription extends SendMessage implements ErrorDescriptionResponse {

  @Getter(onMethod_ = {@JsonGetter("chat_id")})
  private Long errorChatId;
  private String text;
  @Getter(onMethod_ = {@JsonGetter("reply_markup")})
  private ReplyKeyboardMarkup replyMarkup;
}
