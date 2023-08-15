package md.bot.fuel.infrastructure.validation;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

  private List<String> acceptedValues;

  @Override
  public void initialize(ValueOfEnum annotation) {
    acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
        .map(e -> e.name().toLowerCase())
        .collect(toList());
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return acceptedValues.contains(value.toString().toLowerCase());
  }
}