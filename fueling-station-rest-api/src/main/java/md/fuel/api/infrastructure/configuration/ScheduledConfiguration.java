package md.fuel.api.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.api.infrastructure.client.AnreApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(value = "app.scheduled-fetching.enabled", havingValue = "true")
public class ScheduledConfiguration {

  private final AnreApi anreApi;

  @Scheduled(fixedDelayString = "#{${app.caches.anre-json.expiry-time} * 60000}", initialDelayString = "0")
  public void fetchFillingStationData() {
    log.info("Schedule: Clear anre cache");
    clearCache();
    log.info("Schedule: Fetch filling stations data");
    anreApi.getFillingStationsInfo();
  }

  @CacheEvict(value = "anreCache", cacheManager = "jCacheCacheManager")
  private void clearCache() {
    log.info("Clear anre cache");
  }
}
