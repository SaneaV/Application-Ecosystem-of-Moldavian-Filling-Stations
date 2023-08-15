package md.bot.fuel.rest.request;

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
public class PageRequest {

  @Min(value = 0, message = "Limit value should be equal or greater than zero.")
  @Parameter(name = "limit", description = "Limit of one page of returned results.", example = "20", required = true)
  private int limit;

  @Min(value = 0, message = "Offset value should be equal or greater than zero.")
  @Parameter(name = "offset", description = "The number of items skipped from the beginning of the list.",
      example = "0", required = true)
  private int offset;
}