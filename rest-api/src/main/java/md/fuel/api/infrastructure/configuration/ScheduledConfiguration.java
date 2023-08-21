package md.fuel.api.infrastructure.configuration;

import static md.fuel.api.infrastructure.configuration.EhcacheConfiguration.ANRE_CACHE;

import lombok.RequiredArgsConstructor;
import md.fuel.api.infrastructure.repository.AnreApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.scheduled-fetching", havingValue = "true")
public class ScheduledConfiguration {

  private final AnreApi anreApi;

  @Scheduled(fixedDelayString = "#{${cache.expiry.time} * 60000}", initialDelayString = "0")
  public void fetchFillingStationData() {
    clearCache();
    anreApi.getFillingStationsInfo();
  }

  @CacheEvict(value = ANRE_CACHE, cacheManager = "jCacheCacheManager")
  private void clearCache() {
  }
}
