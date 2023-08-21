package md.fuel.api.infrastructure.repository;

import static java.util.stream.Collectors.toList;
import static md.fuel.api.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.infrastructure.exception.model.EntityNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.anre-stub.enabled", havingValue = "true")
public class AnreApiStubImpl implements AnreApi {

  private static final List<FillingStation> FILLING_STATIONS = new ArrayList<>();

  private static final String ERROR_MESSAGE = "Can't read stub ANRE json file";
  private static final String ERROR_REASON_CODE = "FILE_NOT_FOUND";
  private static final String FILE_NAME = "/anre-stub.json";

  private final ObjectMapper objectMapper;
  private final AnreApiMapper mapper;

  @Override
  @Cacheable(value = ANRE_CACHE, cacheManager = "jCacheCacheManager")
  public List<FillingStation> getFillingStationsInfo() {
    if (FILLING_STATIONS.size() != 0) {
      return FILLING_STATIONS;
    }

    try (InputStream is = AnreApiStubImpl.class.getResourceAsStream(FILE_NAME)) {
      final List<FillingStationApi> fillingStationApis = objectMapper.readValue(is, new TypeReference<>() {
      });

      final List<FillingStation> fillingStations = fillingStationApis.stream()
          .map(mapper::toEntity)
          .collect(toList());

      FILLING_STATIONS.addAll(fillingStations);
      return FILLING_STATIONS;
    } catch (IOException e) {
      throw new EntityNotFoundException(ERROR_MESSAGE, ERROR_REASON_CODE);
    }
  }
}
