package md.fuel.bot.infrastructure.configuration;

import static lombok.AccessLevel.PRIVATE;

import java.net.URI;
import java.util.List;
import java.util.stream.IntStream;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.configuration.ApiConfiguration.Details;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class PathUtils {

  public static URI resolve(String path, ApiConfiguration configuration, List<Object> parameters, String... params) {
    final Details details = configuration.getPaths().get(path);
    final String baseUrl = configuration.getBasePath();
    final String pathToApi = details.getPath();

    final UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(baseUrl)
        .path(pathToApi);

    log.info("Create uri for path: {}{} and params: {}", baseUrl, pathToApi, params);

    return addParameters(uriComponentsBuilder, details.getParameters(), parameters)
        .build((Object[]) params);
  }

  private static UriComponentsBuilder addParameters(UriComponentsBuilder uriComponentsBuilder, List<String> parameterName,
      List<Object> parameterValue) {
    IntStream.range(0, parameterName.size())
        .forEach(i -> uriComponentsBuilder.queryParam(parameterName.get(i), parameterValue.get(i)));
    return uriComponentsBuilder;
  }
}
