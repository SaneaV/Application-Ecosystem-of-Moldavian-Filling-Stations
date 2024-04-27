package md.fuel.api.infrastructure.mapper;

import static lombok.AccessLevel.PRIVATE;
import static md.fuel.api.rest.request.SortOrder.ASC;
import static md.fuel.api.rest.request.SortOrder.DESC;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.rest.request.SortingQuery;

@Slf4j
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
        log.info("Sort parameter {} without sort order mapped to ascending", sortParam);
        return new SortingQuery(sortParam, ASC);
      }
    }
  }
}