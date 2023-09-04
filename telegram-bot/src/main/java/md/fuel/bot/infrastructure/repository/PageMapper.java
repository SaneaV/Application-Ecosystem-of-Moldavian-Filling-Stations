package md.fuel.bot.infrastructure.repository;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static md.fuel.bot.infrastructure.repository.MapperConstants.EXCEPTION_MAPPING_MESSAGE;
import static md.fuel.bot.infrastructure.repository.MapperConstants.EXCEPTION_METHOD_MESSAGE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import md.fuel.bot.domain.Page;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;

public interface PageMapper {

  default <T> Page<T> toEntity(Result<?> result, Class<T> targerClass) {
    final List<?> sourceData = result.items();

    if (Objects.isNull(sourceData) || sourceData.isEmpty()) {
      return new Page<>(0, emptyList());
    }

    final Class<?> sourceType = sourceData.get(0).getClass();

    final Method method = stream(this.getClass().getDeclaredMethods())
        .filter(m -> m.getParameterTypes().length == 1)
        .filter(m -> m.getParameterTypes()[0].isAssignableFrom(sourceType))
        .filter(m -> m.getReturnType().isAssignableFrom(targerClass))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(EXCEPTION_METHOD_MESSAGE));

    final List<T> targetItems = sourceData.stream()
        .map(sourceType::cast)
        .map(obj -> {
          try {
            return method.invoke(this, obj);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InfrastructureException(EXCEPTION_MAPPING_MESSAGE);
          }
        })
        .map(targerClass::cast)
        .toList();

    return new Page<>(result.totalResults(), targetItems);
  }
}
