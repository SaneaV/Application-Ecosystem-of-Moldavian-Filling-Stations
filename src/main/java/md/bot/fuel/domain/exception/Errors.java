package md.bot.fuel.domain.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Errors {

  @Getter(onMethod_ = {@JsonGetter("Error")})
  private final List<GatewayError> error = new ArrayList<>();

  public void addError(GatewayError error) {
    this.error.add(error);
  }
}
