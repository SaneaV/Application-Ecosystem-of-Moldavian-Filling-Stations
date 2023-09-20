package md.fuel.bot.infrastructure.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserBlockedEvent extends ApplicationEvent {

  private final Long userId;

  public UserBlockedEvent(Object source, Long userId) {
    super(source);
    this.userId = userId;
  }
}