package md.fuel.api.infrastructure.mapper;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import md.fuel.api.domain.criteria.BaseFillingStationCriteria;
import md.fuel.api.domain.criteria.LimitFillingStationCriteria;
import md.fuel.api.rest.request.BaseFillingStationRequest;
import md.fuel.api.rest.request.LimitFillingStationRequest;
import md.fuel.api.rest.request.PageRequest;
import md.fuel.api.rest.request.SortingQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {

  BaseFillingStationCriteria toEntity(BaseFillingStationRequest request);

  @Mapping(target = "sorting", source = "sorting", qualifiedByName = "toSortingQueryList")
  @Mapping(target = "pageLimit", ignore = true)
  @Mapping(target = "pageOffset", ignore = true)
  LimitFillingStationCriteria toEntity(LimitFillingStationRequest request);

  @Mapping(target = "sorting", source = "limitFillingStationRequest.sorting", qualifiedByName = "toSortingQueryList")
  @Mapping(target = "pageLimit", source = "pageRequest.limit")
  @Mapping(target = "pageOffset", source = "pageRequest.offset")
  LimitFillingStationCriteria toEntity(LimitFillingStationRequest limitFillingStationRequest, PageRequest pageRequest);

  @Named("toSortingQueryList")
  default List<SortingQuery> toSortingQueryList(List<String> sorting) {
    final List<SortingQuery> sortingQueryList = sorting.stream()
        .map(String::toLowerCase)
        .map(SortParameterMapper::resolveParams)
        .collect(toList());
    Collections.reverse(sortingQueryList);
    return sortingQueryList;
  }
}