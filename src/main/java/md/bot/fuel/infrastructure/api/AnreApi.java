package md.bot.fuel.infrastructure.api;

import java.util.List;
import md.bot.fuel.domain.FuelStation;

public interface AnreApi {

  List<FuelStation> getFuelStationsInfo();
}
