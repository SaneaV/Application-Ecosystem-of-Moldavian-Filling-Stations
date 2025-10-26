package md.electric.api.infrastructure.client.plugshare;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.ConnectorType;
import md.electric.api.domain.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlugShareApiMapper {

  @Mapping(target = "connectorTypes", source = "api.connectorTypes", qualifiedByName = "stringListToConnectorTypeSet")
  ElectricStation toEntity(PlugShareResponse api, Location location);

  @Named("stringListToConnectorTypeSet")
  default Set<ConnectorType> stringListToConnectorTypeSet(String[] connectorTypes) {
    if (connectorTypes == null) {
      return Set.of();
    }
    return Arrays.stream(connectorTypes)
        .map(ConnectorType::fromValue)
        .collect(Collectors.toSet());
  }
}