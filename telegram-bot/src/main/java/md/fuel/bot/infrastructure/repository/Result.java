package md.fuel.bot.infrastructure.repository;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Result<T> {

  private final int totalResults;
  private final List<T> items;
}
