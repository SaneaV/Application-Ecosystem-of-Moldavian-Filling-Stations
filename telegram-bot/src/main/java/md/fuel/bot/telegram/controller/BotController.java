package md.fuel.bot.telegram.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.telegram.FillingStationTelegramBot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class BotController {

  private static final String CHAT_ID_ATTRIBUTE = "chatId";

  private final FillingStationTelegramBot fillingStationTelegramBot;

  @PostMapping(value = "/callback/${telegram.bot-token}")
  public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update, WebRequest webRequest) {
    webRequest.setAttribute(CHAT_ID_ATTRIBUTE, update.getMessage().getChat().getId(), SCOPE_REQUEST);

    final BotApiMethod<?> botApiMethod = fillingStationTelegramBot.onWebhookUpdateReceived(update);
    return ok().body(botApiMethod);
  }
}
