package md.fuel.api.rest.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ParameterObject
public class BaseFillingStationRequest implements Serializable {

  @DecimalMin(value = "-90.0", message = "Latitude value should be between -90 and 90.")
  @DecimalMax(value = "90.0", message = "Latitude value should be between -90 and 90.")
  @Parameter(name = "latitude", description = "Latitude of the starting point of the search.", example = "46.0", required = true)
  private double latitude;

  @DecimalMin(value = "-90.0", message = "Longitude value should be between -90 and 90.")
  @DecimalMax(value = "90.0", message = "Longitude value should be between -90 and 90.")
  @Parameter(name = "longitude", description = "Longitude of the starting point of the search.", example = "28.0", required =
      true)
  private double longitude;

  @DecimalMin(value = "0.0", message = "Radius value should be equal or greater than zero.")
  @Parameter(name = "radius", description = "Search radius for petrol stations in metres.", example = "5000", required = true)
  private double radius;
}
