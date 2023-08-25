package md.fuel.bot.domain;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Page<T> implements Serializable {

  private final int totalResults;
  private final List<T> items;
}
