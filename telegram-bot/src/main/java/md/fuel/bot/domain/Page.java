package md.fuel.bot.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Page<T> {

  private final int totalResults;
  private final List<T> items;
}
