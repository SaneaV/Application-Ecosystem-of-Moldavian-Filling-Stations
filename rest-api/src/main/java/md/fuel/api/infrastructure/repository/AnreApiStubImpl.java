package md.fuel.api.infrastructure.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.domain.FuelPrice;
import md.fuel.api.infrastructure.exception.model.InfrastructureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.anre-stub.enabled", havingValue = "true")
public class AnreApiStubImpl implements AnreApi {

  private static final List<FillingStation> FILLING_STATIONS = new ArrayList<>();

  private static final String ERROR_MESSAGE = "Can't read stub ANRE json file.";
  private static final String ERROR_REASON_CODE = "FILE_NOT_FOUND";

  private final ObjectMapper objectMapper;
  private final AnreApiMapper mapper;
  private final String fileName;

  public AnreApiStubImpl(ObjectMapper objectMapper, AnreApiMapper mapper,
      @Value(value = "${app.anre-stub.file-name}") String fileName) {
    this.objectMapper = objectMapper;
    this.mapper = mapper;
    this.fileName = fileName;
  }

  @Override
  @Cacheable(value = "anreCache", cacheManager = "jCacheCacheManager")
  public List<FillingStation> getFillingStationsInfo() {
    log.info("Fetching all filling stations info from the local file");
    if (FILLING_STATIONS.size() != 0) {
      log.info("Return loaded data");
      return FILLING_STATIONS;
    }

    try (InputStream is = this.getClass().getResourceAsStream(fileName)) {
      final List<FillingStationApi> fillingStationApis = objectMapper.readValue(is, new TypeReference<>() {
      });
      log.info("Read data from the file");
      final List<FillingStation> fillingStations = fillingStationApis.stream()
          .map(mapper::toEntity)
          .toList();

      FILLING_STATIONS.addAll(fillingStations);
      return FILLING_STATIONS;
    } catch (IOException e) {
      throw new InfrastructureException(ERROR_MESSAGE, ERROR_REASON_CODE);
    }
  }

  @Override
  @Cacheable(value = "anrePriceCache", cacheManager = "jCacheCacheManager")
  public FuelPrice getAnrePrices() {
    log.info("Fetching dummy prices for fuel");

    final String date = LocalDate.now().toString();

    return new FuelPrice(getRandomDouble(), getRandomDouble(), date);
  }

  private Double getRandomDouble() {
    final Random rand = new Random();
    final MathContext mathContext = new MathContext(2);

    return BigDecimal.valueOf(rand.nextDouble(50))
        .round(mathContext)
        .doubleValue();
  }
}