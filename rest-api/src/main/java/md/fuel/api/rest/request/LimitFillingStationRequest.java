package md.fuel.api.rest.request;

import static lombok.AccessLevel.NONE;

import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ParameterObject
public class LimitFillingStationRequest extends BaseFillingStationRequest {

  @Setter(NONE)
  @Min(value = 1, message = "Limit in radius value should be equal or greater than one.")
  @Parameter(name = "limit_in_radius", description = "Number of filling stations allowed to be displayed within a certain radius.",
      example = "10", required = true)
  private int limitInRadius;

  public void setLimit_in_radius(int limitInRadius) {
    this.limitInRadius = limitInRadius;
  }
}
