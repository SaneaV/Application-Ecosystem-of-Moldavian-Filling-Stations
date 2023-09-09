package md.fuel.api.rest.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Page with list of items and total results.")
public record PageDto<T>(
    @Schema(description = "Total number of filling stations in radius.", example = "10", requiredMode = REQUIRED)
    int totalResults,
    @Schema(description = "List of filling stations.", requiredMode = REQUIRED)
    List<T> items) {

  @JsonCreator
  public PageDto(@JsonProperty(value = "totalResults", required = true) int totalResults,
      @JsonProperty(value = "items", required = true) List<T> items) {
    this.totalResults = totalResults;
    this.items = items;
  }
}
