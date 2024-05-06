package md.telegram.lib.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:telegram-bot-lib.properties")
public class PropertyLoader {

}