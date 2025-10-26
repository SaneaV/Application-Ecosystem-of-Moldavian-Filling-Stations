package md.electric.api.infrastructure.mapper;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import md.electric.api.domain.criteria.ElectricStationCriteria;
import md.electric.api.domain.criteria.SortingQuery;
import md.electric.api.rest.request.ElectricStationRequest;
import md.electric.api.rest.request.PageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {

  @Mapping(target = "sorting", source = "sorting", qualifiedByName = "toSortingQueryList")
  @Mapping(target = "pageLimit", ignore = true)
  @Mapping(target = "pageOffset", ignore = true)
  ElectricStationCriteria toEntity(ElectricStationRequest request);

  @Mapping(target = "sorting", source = "request.sorting", qualifiedByName = "toSortingQueryList")
  @Mapping(target = "pageLimit", source = "pageRequest.limit")
  @Mapping(target = "pageOffset", source = "pageRequest.offset")
  ElectricStationCriteria toEntity(ElectricStationRequest request, PageRequest pageRequest);

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