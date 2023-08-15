package md.bot.fuel.rest.wrapper;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "Page with list of items and total results.")
public class PageDto<T> {

  @Schema(description = "Total number of fuel stations in radius.", example = "10", requiredMode = REQUIRED)
  private final int totalResults;
  @Schema(description = "List of fuel stations.", requiredMode = REQUIRED)
  private final List<T> items;

  @JsonCreator
  public PageDto(@JsonProperty(value = "totalResults", required = true) int totalResults,
      @JsonProperty(value = "items", required = true) List<T> items) {
    this.totalResults = totalResults;
    this.items = items;
  }
}
