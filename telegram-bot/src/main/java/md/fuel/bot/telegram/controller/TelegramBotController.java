package md.fuel.bot.telegram.controller;

import static java.util.Objects.isNull;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.configuration.RequestRateValidator;
import md.fuel.bot.telegram.utils.ChatInfoUtil;
import md.fuel.bot.telegram.validation.UserStatusValidator;
import md.telegram.lib.TelegramBotWebhook;
import md.telegram.lib.contoller.BotController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TelegramBotController implements BotController {

  private final TelegramBotWebhook telegramBotWebhook;
  private final RequestRateValidator requestRateValidator;
  private final UserStatusValidator userStatusValidator;
  private final ChatInfoUtil chatInfoUtil;
  private final ChatInfoHolder chatInfoHolder;

  @Override
  public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update) {
    if (isNull(update.getMessage())
        && !update.hasCallbackQuery()
        && userStatusValidator.checkIfUserBlockedBot(update)) {
      return noContent().build();
    }

    chatInfoUtil.setChatInfo(update);
    requestRateValidator.validateRequest(chatInfoHolder.getUserId());

    final BotApiMethod<?> botApiMethod = telegramBotWebhook.onWebhookUpdateReceived(update);
    return ok().body(botApiMethod);
  }
}