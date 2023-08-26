package md.fuel.bot.infrastructure.repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Result<T>(
    int totalResults,
    List<T> items) {

  @JsonCreator
  public Result(@JsonProperty("totalResults") int totalResults,
      @JsonProperty("items") List<T> items) {
    this.totalResults = totalResults;
    this.items = items;
  }
}
