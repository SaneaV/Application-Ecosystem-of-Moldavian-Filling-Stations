package md.fuel.bot.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import md.fuel.bot.infrastructure.repository.GatewayError;

@Getter
@AllArgsConstructor
public class Errors<T extends GatewayError> {

  @Getter(onMethod_ = {@JsonGetter("Error")})
  private final List<T> error = new ArrayList<>();
}