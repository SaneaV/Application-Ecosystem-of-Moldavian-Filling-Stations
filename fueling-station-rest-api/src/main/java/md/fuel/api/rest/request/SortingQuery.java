package md.fuel.api.rest.request;

import static java.util.Objects.isNull;
import static md.fuel.api.rest.request.SortOrder.ASC;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class SortingQuery {

  private final String sortOrderBy;
  private final SortOrder sortOrder;

  public boolean isAscending() {
    return isNull(this.sortOrder) || ASC.equals(this.sortOrder);
  }
}