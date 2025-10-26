package md.electric.api.rest.request;

import static java.util.Collections.reverse;
import static java.util.Collections.singletonList;
import static lombok.AccessLevel.NONE;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class ElectricStationRequest implements Serializable {

  private static final String DEFAULT_SORTING = "+distance";
  private static final List<String> DEFAULT_SORTING_LIST = singletonList(DEFAULT_SORTING);

  @NotNull
  @DecimalMin(value = "-90.0", message = "Latitude value should be between -90 and 90.")
  @DecimalMax(value = "90.0", message = "Latitude value should be between -90 and 90.")
  private double latitude;

  @NotNull
  @DecimalMin(value = "-90.0", message = "Longitude value should be between -90 and 90.")
  @DecimalMax(value = "90.0", message = "Longitude value should be between -90 and 90.")
  private double longitude;

  @NotNull
  @DecimalMin(value = "0.0", message = "Radius value should be equal or greater than zero.")
  private double radius;

  @Setter(NONE)
  private List<String> sorting = DEFAULT_SORTING_LIST;

  @Setter(NONE)
  private List<String> connectors;

  public void setSorting(List<String> sorting) {
    reverse(sorting);
    this.sorting = sorting.isEmpty() ? DEFAULT_SORTING_LIST : sorting;
  }

  public void setConnector_type(List<String> connectors) {
    this.connectors = connectors;
  }
}