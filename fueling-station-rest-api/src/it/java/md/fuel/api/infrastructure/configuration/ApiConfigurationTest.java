package md.fuel.api.infrastructure.configuration;

import static java.util.Arrays.asList;

import java.time.Duration;
import java.util.Map;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ApiConfigurationTest {

  @Bean
  public ApiConfiguration apiConfiguration(Integer availableTcpPort) {
    final ApiConfiguration apiConfiguration = new ApiConfiguration();
    apiConfiguration.setRetryCount(1);
    apiConfiguration.setTimeDuration(Duration.ofMillis(1));
    apiConfiguration.setRetryable(asList(404, 429));
    apiConfiguration.setBasePath(String.format("http://localhost:%s", availableTcpPort));
    apiConfiguration.setPaths(Map.of(
        "all-filling-stations", "/",
        "today-fuel-price", "/plafon"));
    return apiConfiguration;
  }
}
