package md.fuel.bot.domain;

import java.lang.reflect.Method;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetterSetterPair {

  private Method getter;
  private Method setter;

  public boolean hasGetterAndSetter() {
    return this.getter != null && this.setter != null;
  }
}