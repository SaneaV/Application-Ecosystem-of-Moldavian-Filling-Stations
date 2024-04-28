package md.fuel.bot.telegram.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.event.UserBlockedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStatusValidatorImpl implements UserStatusValidator {

  private static final String KICKED_STATUS = "kicked";

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public boolean checkIfUserBlockedBot(Update update) {
    if (KICKED_STATUS.equalsIgnoreCase(update.getMyChatMember().getNewChatMember().getStatus())) {
      log.info("Trigger event to delete user due to bot blocking");
      final Long userId = update.getMyChatMember().getFrom().getId();
      final UserBlockedEvent userBlockedEvent = new UserBlockedEvent(this, userId);
      applicationEventPublisher.publishEvent(userBlockedEvent);
    }
    return true;
  }
}
