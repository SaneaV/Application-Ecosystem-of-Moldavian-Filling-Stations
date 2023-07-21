package md.bot.fuel.infrastructure.api;

import md.bot.fuel.domain.FuelStation;

import java.util.List;

public interface AnreApi {

    List<FuelStation> getFuelStationsInfo();
}
