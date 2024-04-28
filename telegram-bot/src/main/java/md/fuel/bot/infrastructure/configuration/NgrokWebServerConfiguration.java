package md.fuel.bot.infrastructure.configuration;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "ngrok.enabled", havingValue = "true")
public class NgrokWebServerConfiguration {

  @Value("${server.port}")
  private String serverPort;

  @Value("${ngrok.token}")
  private String ngrokToken;

  @Bean
  public Tunnel ngrokTunnel(NgrokClient ngrokClient, CreateTunnel createTunnel) {
    log.info("Create ngrok tunnel");
    return ngrokClient.connect(createTunnel);
  }

  @Bean
  public JavaNgrokConfig javaNgrokConfig() {
    log.info("Create ngrok config");
    return new JavaNgrokConfig.Builder()
        .withAuthToken(ngrokToken)
        .build();
  }

  @Bean
  public NgrokClient ngrokClient(JavaNgrokConfig javaNgrokConfig) {
    log.info("Create ngrok client");
    return new NgrokClient.Builder()
        .withJavaNgrokConfig(javaNgrokConfig)
        .build();
  }

  @Bean
  public CreateTunnel createTunnel() {
    log.info("Configure tunnel with server port: {}", serverPort);
    final int port = Integer.parseInt(serverPort);
    return new CreateTunnel.Builder()
        .withAddr(port)
        .build();
  }
}
