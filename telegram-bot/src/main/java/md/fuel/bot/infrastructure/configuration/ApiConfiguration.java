package md.fuel.bot.infrastructure.configuration;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties("api-configuration")
@PropertySource(value = "classpath:api-configuration.yaml", factory = YamlPropertySourceFactory.class)
public class ApiConfiguration {

  private Integer retryCount;
  private Duration timeDuration;
  private List<Integer> retryable;
  private String basePath;
  private Map<String, String> paths;
}
