package md.fuel.bot.infrastructure.configuration;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.ALL_FILLING_STATIONS_PAGE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.BEST_FUEL_PRICE_PAGE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.FUEL_TYPE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.LAST_UPDATE_PATH;
import static md.fuel.bot.infrastructure.configuration.ResourcePath.NEAREST_PATH;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import md.fuel.bot.infrastructure.configuration.ApiConfiguration.Details;
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
    apiConfiguration.setPaths(
        Map.of(
            ALL_FILLING_STATIONS_PAGE_PATH, buildDetails("/page/filling-station",
                asList("latitude", "longitude", "radius", "limit_in_radius", "sorting", "limit", "offset")),
            NEAREST_PATH, buildDetails("/filling-station/nearest",
                asList("latitude", "longitude", "radius")),
            BEST_FUEL_PRICE_PAGE_PATH, buildDetails("/page/filling-station/{fuel-type}",
                asList("latitude", "longitude", "radius", "limit_in_radius", "sorting", "limit", "offset")),
            LAST_UPDATE_PATH, buildDetails("/filling-station/last-update", emptyList()),
            FUEL_TYPE_PATH, buildDetails("/filling-station/fuel-type", emptyList())));
    return apiConfiguration;
  }

  private Details buildDetails(String path, List<String> parameters) {
    final Details details = new Details();
    details.setParameters(parameters);
    details.setPath(path);
    return details;
  }
}
