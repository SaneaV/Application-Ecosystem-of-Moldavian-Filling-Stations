package md.electric.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan("md.electric.api.infrastructure.configuration")
@ComponentScan(basePackages = {"md.electric.api", "md.cache.lib"})
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}