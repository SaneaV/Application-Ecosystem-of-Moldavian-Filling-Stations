package md.fuel.bot.infrastructure.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class Result<T> {

  private final int totalResults;
  private final List<T> items;

  @JsonCreator
  public Result(@JsonProperty("totalResults") int totalResults,
      @JsonProperty("items") List<T> items) {
    this.totalResults = totalResults;
    this.items = items;
  }
}
