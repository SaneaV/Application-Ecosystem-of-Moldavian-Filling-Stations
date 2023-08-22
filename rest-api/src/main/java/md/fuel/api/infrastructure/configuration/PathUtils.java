package md.fuel.api.infrastructure.configuration;

import static lombok.AccessLevel.PRIVATE;

import java.net.URI;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor(access = PRIVATE)
public class PathUtils {

  public static URI resolve(String path, ApiConfiguration configuration, String... params) {
    final String pathToApi = configuration.getPaths().get(path);
    final String baseUrl = configuration.getBasePath();

    return UriComponentsBuilder.fromUriString(baseUrl)
        .path(pathToApi)
        .build((Object[]) params);
  }
}
