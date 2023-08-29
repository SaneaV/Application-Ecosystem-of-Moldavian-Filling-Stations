package md.fuel.api.rest.request;

import static java.util.Objects.isNull;
import static md.fuel.api.rest.request.SortOrder.ASC;

public record SortingQuery(String sortOrderBy, SortOrder sortOrder) {

  public boolean isAscending() {
    return isNull(this.sortOrder) || ASC.equals(this.sortOrder);
  }
}