package md.electric.api.rest.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class PageRequest {

  @Min(value = 0, message = "Limit value should be equal or greater than zero.")
  private int limit;

  @Min(value = 0, message = "Offset value should be equal or greater than zero.")
  private int offset;
}