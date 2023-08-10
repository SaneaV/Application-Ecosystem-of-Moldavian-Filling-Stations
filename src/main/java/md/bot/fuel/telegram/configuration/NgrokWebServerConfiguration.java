package md.bot.fuel.telegram.configuration;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "ngrok.enabled", havingValue = "true")
public class NgrokWebServerConfiguration {

    @Value("${server.port}")
    private String serverPort;

    @Value("${ngrok.token}")
    private String ngrokToken;

    @Bean
    public Tunnel ngrokTunnel(NgrokClient ngrokClient, CreateTunnel createTunnel) {
        return ngrokClient.connect(createTunnel);
    }

    @Bean
    public JavaNgrokConfig javaNgrokConfig() {
        return new JavaNgrokConfig.Builder()
                .withAuthToken(ngrokToken)
                .build();
    }

    @Bean
    public NgrokClient ngrokClient(JavaNgrokConfig javaNgrokConfig) {
        return new NgrokClient.Builder()
                .withJavaNgrokConfig(javaNgrokConfig)
                .build();
    }

    @Bean
    public CreateTunnel createTunnel() {
        final int port = Integer.parseInt(serverPort);
        return new CreateTunnel.Builder()
                .withAddr(port)
                .build();
    }
}
