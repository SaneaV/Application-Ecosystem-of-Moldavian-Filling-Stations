package md.fuel.bot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StationType {
  FUEL("FUEL"),
  ELECTRIC("ELECTRIC");

  private final String value;
}