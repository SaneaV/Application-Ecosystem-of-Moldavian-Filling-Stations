package md.fuel.bot.infrastructure.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.jpa.UserDataAdapter;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserBlockedEventListener implements ApplicationListener<UserBlockedEvent> {

  private final UserDataAdapter userDataAdapter;

  @Override
  public void onApplicationEvent(UserBlockedEvent event) {
    log.info("Delete user due to bot blocking event");
    userDataAdapter.delete(event.getUserId());
  }
}