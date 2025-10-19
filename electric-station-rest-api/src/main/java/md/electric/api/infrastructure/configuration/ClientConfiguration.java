package md.electric.api.infrastructure.configuration;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties("client-configuration")
@PropertySource(value = "classpath:client-configuration.yaml", factory = YamlPropertySourceFactory.class)
public class ClientConfiguration {

  private Integer retryCount;
  private Duration timeDuration;
  private List<Integer> retryable;
}