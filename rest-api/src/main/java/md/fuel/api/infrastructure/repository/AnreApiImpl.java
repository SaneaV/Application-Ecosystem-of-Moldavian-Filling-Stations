package md.fuel.api.infrastructure.repository;

import static md.fuel.api.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;

import java.util.List;
import md.fuel.api.domain.FillingStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@ConditionalOnProperty(value = "app.anre-stub.enabled", havingValue = "false")
public class AnreApiImpl implements AnreApi {

  private final WebClient webClient;
  private final AnreApiMapper mapper;
  private final String anreApiPath;

  public AnreApiImpl(WebClient webClient, AnreApiMapper mapper,
      @Value("${anre.api.path}") String anreApiPath) {
    this.webClient = webClient;
    this.mapper = mapper;
    this.anreApiPath = anreApiPath;
  }

  @Override
  @Cacheable(value = ANRE_CACHE, cacheManager = "jCacheCacheManager")
  public List<FillingStation> getFillingStationsInfo() {
    return webClient.get()
        .uri(anreApiPath)
        .retrieve()
        .bodyToFlux(FillingStationApi.class)
        .map(mapper::toEntity)
        .collectList()
        .block();
  }
}
