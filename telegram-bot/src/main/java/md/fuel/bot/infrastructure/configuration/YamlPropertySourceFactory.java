package md.fuel.bot.infrastructure.configuration;

import java.util.Objects;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

@RequiredArgsConstructor
public class YamlPropertySourceFactory implements PropertySourceFactory {

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
    final YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(resource.getResource());
    final Properties properties = factory.getObject();
    assert properties != null;
    return new PropertiesPropertySource(Objects.requireNonNull(resource.getResource().getFilename()), properties);
  }
}
