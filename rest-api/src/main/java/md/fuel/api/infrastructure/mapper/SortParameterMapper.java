package md.fuel.api.infrastructure.mapper;

import static lombok.AccessLevel.PRIVATE;
import static md.fuel.api.rest.request.SortOrder.ASC;
import static md.fuel.api.rest.request.SortOrder.DESC;

import lombok.NoArgsConstructor;
import md.fuel.api.rest.request.SortingQuery;

@NoArgsConstructor(access = PRIVATE)
public class SortParameterMapper {

  private static final char MINUS = '-';
  private static final char PLUS = '+';

  public static SortingQuery resolveParams(String sortParam) {
    switch (sortParam.charAt(0)) {
      case MINUS -> {
        return new SortingQuery(sortParam.substring(1), DESC);
      }
      case PLUS -> {
        return new SortingQuery(sortParam.substring(1), ASC);
      }
      default -> {
        return new SortingQuery(sortParam, ASC);
      }
    }
  }
}