package md.fuel.bot.infrastructure.configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CaffeineConfiguration {

  @Bean
  public LoadingCache<Long, Integer> caffeineInstance(@Value("${telegram.bot.requests-time-reset}") int duration) {
    log.info("Configure Caffeine bean with expireAfterWrite = {} seconds", duration);

    return Caffeine.newBuilder()
        .expireAfterWrite(duration, SECONDS)
        .build(key -> 0);
  }
}