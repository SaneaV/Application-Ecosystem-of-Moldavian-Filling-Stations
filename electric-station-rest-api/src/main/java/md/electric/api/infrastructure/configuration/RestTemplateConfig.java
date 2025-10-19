package md.electric.api.infrastructure.configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_PLAIN;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  private static final String USER_AGENT_HEADER = "User-Agent";

  @Value("${app.electric-station.location-iq.user-agent}")
  private String userAgent;

  @Bean
  public RestTemplate restTemplate() {
    final RestTemplate restTemplate = new RestTemplate();

    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Arrays.asList(APPLICATION_JSON, APPLICATION_OCTET_STREAM, TEXT_PLAIN, TEXT_HTML));

    restTemplate.getMessageConverters().add(0, converter);

    restTemplate.getInterceptors().add((request, body, execution) -> {
      request.getHeaders().add(USER_AGENT_HEADER, userAgent);
      return execution.execute(request, body);
    });

    return restTemplate;
  }
}