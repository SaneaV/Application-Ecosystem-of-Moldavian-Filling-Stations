package md.electric.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class PageDto<T> {

  private final int totalResults;
  private final List<T> items;

  @JsonCreator
  public PageDto(@JsonProperty(value = "totalResults", required = true) int totalResults,
      @JsonProperty(value = "items", required = true) List<T> items) {
    this.totalResults = totalResults;
    this.items = items;
  }
}