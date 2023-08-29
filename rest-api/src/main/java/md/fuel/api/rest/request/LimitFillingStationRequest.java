package md.fuel.api.rest.request;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.NONE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ParameterObject
public class LimitFillingStationRequest extends BaseFillingStationRequest {

  private static final String DEFAULT_SORTING = "+distance";
  private static final List<String> DEFAULT_SORTING_LIST = singletonList(DEFAULT_SORTING);

  @Setter(NONE)
  @Min(value = 1, message = "Limit in radius value should be equal or greater than one.")
  @Parameter(name = "limit_in_radius", description = "Number of filling stations allowed to be displayed within a certain radius.",
      example = "10", required = true)
  private int limitInRadius;

  @Setter(NONE)
  @Parameter(name = "sorting", description = "Sort order by and sort order parameters.", example = "-distance,+gas,petrol",
      schema = @Schema(allowableValues = {"+distance", "-distance", "+name", "-name", "+petrol", "-petrol", "+diesel", "-diesel",
          "+gas", "-gas"}, defaultValue = DEFAULT_SORTING))
  private List<String> sorting = DEFAULT_SORTING_LIST;

  public void setLimit_in_radius(int limitInRadius) {
    this.limitInRadius = limitInRadius;
  }

  public void setSorting(List<String> sorting) {
    if (isNull(sorting) || sorting.isEmpty()) {
      this.sorting = DEFAULT_SORTING_LIST;
    } else {
      this.sorting = sorting;
    }
  }
}
