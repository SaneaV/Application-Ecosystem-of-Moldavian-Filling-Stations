package md.fuel.api.infrastructure.utils;

import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MultiComparator<T> implements Comparator<T> {

  private final List<Comparator<T>> comparators;

  @Override
  public int compare(T o1, T o2) {
    for (Comparator<T> comparator : comparators) {
      final int result = comparator.compare(o1, o2);
      if (result != 0) {
        return result;
      }
    }
    return 0;
  }

  public static <T> void sort(List<T> list, List<Comparator<T>> comparators) {
    list.sort(new MultiComparator<>(comparators));
  }
}