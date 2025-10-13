package md.fuel.api.infrastructure.configuration;

import static lombok.AccessLevel.PRIVATE;

import java.net.URI;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class PathUtils {

  public static URI resolve(String path, ApiConfiguration configuration, String... params) {
    final String baseUrl = configuration.getBasePath();
    final String pathToApi = configuration.getPaths().get(path);

    log.info("Create uri for path: {}{} and params: {}", baseUrl, pathToApi, params);

    return UriComponentsBuilder.fromUriString(baseUrl)
        .path(pathToApi)
        .build((Object[]) params);
  }
}
