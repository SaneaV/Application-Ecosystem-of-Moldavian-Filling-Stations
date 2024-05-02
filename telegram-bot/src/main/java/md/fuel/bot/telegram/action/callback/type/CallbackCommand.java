package md.fuel.bot.telegram.action.callback.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CallbackCommand {
  ALL_FILLING_STATIONS_IN_RADIUS(1),
  BEST_FUEL_IN_RADIUS(2);

  private final int commandId;
}
