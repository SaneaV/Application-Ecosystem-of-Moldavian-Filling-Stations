package md.fuel.bot.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FuelType {

  private final List<String> fuelTypes;
}