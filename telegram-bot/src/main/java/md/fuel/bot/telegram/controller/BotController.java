package md.fuel.bot.telegram.controller;

import static java.util.Objects.isNull;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.event.UserBlockedEvent;
import md.fuel.bot.telegram.FillingStationTelegramBot;
import md.fuel.bot.telegram.configuration.RequestRateValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {

  private static final String CHAT_ID_ATTRIBUTE = "chatId";
  private static final String KICKED_STATUS = "kicked";

  private final FillingStationTelegramBot fillingStationTelegramBot;
  private final RequestRateValidator requestRateValidator;
  private final ApplicationEventPublisher applicationEventPublisher;

  @PostMapping(value = "/callback/${telegram.bot-token}")
  public ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update, WebRequest webRequest) {
    if (checkIfUserBlockedBot(update)) {
      return noContent().build();
    }

    final Long userId = update.getMessage().getChat().getId();
    webRequest.setAttribute(CHAT_ID_ATTRIBUTE, userId, SCOPE_REQUEST);
    requestRateValidator.validateRequest(userId);

    final BotApiMethod<?> botApiMethod = fillingStationTelegramBot.onWebhookUpdateReceived(update);
    return ok().body(botApiMethod);
  }

  private boolean checkIfUserBlockedBot(Update update) {
    if (!isNull(update.getMessage())) {
      return false;
    }

    if (KICKED_STATUS.equalsIgnoreCase(update.getMyChatMember().getNewChatMember().getStatus())) {
      log.info("Trigger event to delete user due to bot blocking");
      final Long userId = update.getMyChatMember().getFrom().getId();
      final UserBlockedEvent userBlockedEvent = new UserBlockedEvent(this, userId);
      applicationEventPublisher.publishEvent(userBlockedEvent);
    }
    return true;
  }
}
