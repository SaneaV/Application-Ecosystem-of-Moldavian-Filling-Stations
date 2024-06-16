package md.cache.lib;

import static java.util.Objects.isNull;
import static org.ehcache.config.units.EntryUnit.ENTRIES;
import static org.ehcache.config.units.MemoryUnit.MB;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

  public static final String J_CACHE_CACHE_MANAGER = "jCacheCacheManager";

  private final CachePropertyWrapper cachePropertyWrapper;

  @Bean(J_CACHE_CACHE_MANAGER)
  public JCacheCacheManager jCacheCacheManager(@Qualifier("cacheManager") CacheManager cacheManager) {
    return new JCacheCacheManager(cacheManager);
  }

  @Bean
  public EhcacheCachingProvider ehcacheCachingProvider() {
    return new EhcacheCachingProvider();
  }

  @Bean
  public CacheManager cacheManager(EhcacheCachingProvider ehcacheCachingProvider) {
    final Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();
    final Map<String, CachePropertyConfig> mapCaches = cachePropertyWrapper.getCaches();

    if (!isNull(mapCaches)) {
      mapCaches.values()
          .forEach(cache -> {
            final String cacheName = cache.getCacheName();
            final Integer onHeapSize = cache.getOnHeapSize();
            final Integer offHeapSize = cache.getOffHeapSize();
            final Integer expiryTime = cache.getExpiryTime();

            log.info("Create cache configuration for {} with {} MB on heap size, {} MB off heap size and {} minutes expiry time",
                cacheName, onHeapSize, offHeapSize, expiryTime);

            caches.put(cacheName, getCacheConfiguration(onHeapSize, offHeapSize, expiryTime));
          });
    }

    final org.ehcache.config.Configuration configuration = new DefaultConfiguration(caches,
        ehcacheCachingProvider.getDefaultClassLoader());

    return ehcacheCachingProvider.getCacheManager(ehcacheCachingProvider.getDefaultURI(), configuration);
  }

  private CacheConfiguration<Object, Object> getCacheConfiguration(Integer onHeapSize, Integer offHeapSize, Integer expiryTime) {
    return CacheConfigurationBuilder
        .newCacheConfigurationBuilder(Object.class, Object.class, getResourcePools(onHeapSize, offHeapSize))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(expiryTime)))
        .build();
  }

  private ResourcePools getResourcePools(Integer onHeapSize, Integer offHeapSize) {
    return ResourcePoolsBuilder.newResourcePoolsBuilder()
        .heap(onHeapSize, ENTRIES)
        .offheap(offHeapSize, MB)
        .build();
  }
}