package md.bot.fuel.infrastructure.api;

import static md.bot.fuel.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;

import java.util.List;
import md.bot.fuel.domain.FuelStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
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
  public List<FuelStation> getFuelStationsInfo() {
    return webClient.get()
        .uri(anreApiPath)
        .retrieve()
        .bodyToFlux(FuelStationApi.class)
        .map(mapper::toEntity)
        .collectList()
        .block();
  }
}
