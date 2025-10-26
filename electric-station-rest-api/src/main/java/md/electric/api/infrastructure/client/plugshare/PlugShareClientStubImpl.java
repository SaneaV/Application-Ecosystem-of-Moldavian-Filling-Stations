package md.electric.api.infrastructure.client.plugshare;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import md.electric.api.domain.ElectricStation;
import md.electric.api.domain.Location;
import md.electric.api.infrastructure.client.locationiq.LocationIqClient;
import md.electric.api.infrastructure.exception.model.InfrastructureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.electric-station.plugshare.stub.enabled", havingValue = "true")
public class PlugShareClientStubImpl implements PlugShareClient {

  private static final List<ElectricStation> ELECTRIC_STATIONS = new ArrayList<>();

  private static final String ERROR_MESSAGE = "Can't read stub PLUGSHARE json file.";
  private static final String ERROR_REASON_CODE = "FILE_NOT_FOUND";
  private static final String MD_CODE = "md";

  private final ObjectMapper objectMapper;
  private final PlugShareApiMapper mapper;
  private final LocationIqClient locationIqClient;
  private final String fileName;

  public PlugShareClientStubImpl(ObjectMapper objectMapper, PlugShareApiMapper mapper,
      LocationIqClient locationIqClient, @Value(value = "${app.electric-station.plugshare.stub.file-name}") String fileName) {
    this.objectMapper = objectMapper;
    this.mapper = mapper;
    this.locationIqClient = locationIqClient;
    this.fileName = fileName;
  }

  @Override
  @Cacheable(value = "plugshareCache", cacheManager = "jCacheCacheManager")
  public List<ElectricStation> fetchStations(Double latitude, Double longitude) {
    log.info("Fetching all electric stations info from the local file");
    if (!ELECTRIC_STATIONS.isEmpty()) {
      log.info("Return loaded data");
      return ELECTRIC_STATIONS;
    }

    try (InputStream is = this.getClass().getResourceAsStream(fileName)) {
      final List<PlugShareResponse> electricStationsResponse = objectMapper.readValue(is, new TypeReference<>() {
      });
      log.info("Read data from the file");
      final List<ElectricStation> fillingStations = electricStationsResponse.stream()
          .map(station -> {
            final Optional<Location> location =
                locationIqClient.getLocationByCoordinates(station.getLatitude(), station.getLongitude(), MD_CODE);
            return location.map(value -> mapper.toEntity(station, value))
                .orElse(null);
          })
          .filter(Objects::nonNull)
          .toList();

      ELECTRIC_STATIONS.addAll(fillingStations);
      return ELECTRIC_STATIONS;
    } catch (IOException e) {
      throw new InfrastructureException(ERROR_MESSAGE, ERROR_REASON_CODE);
    }
  }
}
