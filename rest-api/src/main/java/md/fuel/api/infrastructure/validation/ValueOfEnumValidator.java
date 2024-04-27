package md.fuel.api.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {

  private List<String> acceptedValues;

  @Override
  public void initialize(ValueOfEnum annotation) {
    acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
        .map(e -> e.name().toLowerCase())
        .toList();
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    log.debug("Validate that {} is valid enum type ({})", value, acceptedValues);

    if (value == null) {
      return false;
    }
    return acceptedValues.contains(value.toString().toLowerCase());
  }
}