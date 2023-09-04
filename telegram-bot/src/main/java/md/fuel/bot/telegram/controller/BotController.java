package md.fuel.bot.telegram.controller;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.telegram.FillingStationTelegramBot;
import md.fuel.bot.telegram.configuration.RequestRateValidator;
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
  private final RequestRateValidator requestRateValidator;

  @PostMapping(value = "/callback/${telegram.bot-token}")
  public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update, WebRequest webRequest) {
    final Long userId = update.getMessage().getChat().getId();

    webRequest.setAttribute(CHAT_ID_ATTRIBUTE, userId, SCOPE_REQUEST);
    requestRateValidator.validateRequest(userId);

    final BotApiMethod<?> botApiMethod = fillingStationTelegramBot.onWebhookUpdateReceived(update);
    return ok().body(botApiMethod);
  }
}
