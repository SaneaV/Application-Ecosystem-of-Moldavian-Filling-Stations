package md.fuel.bot.infrastructure.configuration;

import java.util.Objects;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

@Slf4j
@RequiredArgsConstructor
public class YamlPropertySourceFactory implements PropertySourceFactory {

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
    log.info("Initialize yaml properties factory bean");
    final YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource.getResource());
    final Properties properties = factory.getObject();
    return new PropertiesPropertySource(Objects.requireNonNull(resource.getResource().getFilename()), properties);
  }
}
