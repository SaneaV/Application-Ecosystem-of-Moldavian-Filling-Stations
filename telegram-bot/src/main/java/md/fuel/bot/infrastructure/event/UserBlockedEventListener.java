package md.fuel.bot.infrastructure.event;

import lombok.RequiredArgsConstructor;
import md.fuel.bot.infrastructure.jpa.UserDataAdapter;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBlockedEventListener implements ApplicationListener<UserBlockedEvent> {

  private final UserDataAdapter userDataAdapter;

  @Override
  public void onApplicationEvent(UserBlockedEvent event) {
    userDataAdapter.delete(event.getUserId());
  }
}