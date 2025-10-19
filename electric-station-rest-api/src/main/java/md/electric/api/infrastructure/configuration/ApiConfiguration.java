package md.electric.api.infrastructure.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties("plugshare-api")
@PropertySource(value = "classpath:plugshare-api.yaml", factory = YamlPropertySourceFactory.class)
public class ApiConfiguration {

  private String baseUrl;
  private String authorization;
  private Double defaultLatitude;
  private Double defaultLongitude;
  private Double spanLat;
  private Double spanLng;
  private Integer count;
  private String accept;
  private String acceptLanguage;
  private String cacheControl;
  private String pragma;
  private String userAgent;
}