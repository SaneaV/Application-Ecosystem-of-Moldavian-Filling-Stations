package md.fuel.api.infrastructure.repository;

import static md.fuel.api.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;
import static md.fuel.api.infrastructure.configuration.PathUtils.resolve;
import static md.fuel.api.infrastructure.configuration.ResourcePath.ALL_FILLING_STATION_PATH;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import md.fuel.api.domain.FillingStation;
import md.fuel.api.infrastructure.configuration.ApiConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.anre-stub.enabled", havingValue = "false")
public class AnreApiImpl implements AnreApi {

  private final WebClient webClient;
  private final AnreApiMapper mapper;
  private final ApiConfiguration apiConfiguration;

  @Override
  @Cacheable(value = ANRE_CACHE, cacheManager = "jCacheCacheManager")
  public List<FillingStation> getFillingStationsInfo() {
    final URI uri = fromUri(resolve(ALL_FILLING_STATION_PATH, apiConfiguration))
        .build().toUri();

    return webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(FillingStationApi.class)
        .map(mapper::toEntity)
        .collectList()
        .block();
  }
}
