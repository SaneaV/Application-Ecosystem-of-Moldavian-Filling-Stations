package md.fuel.bot.configuration;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.alexdlaird.ngrok.protocol.Tunnel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@RequiredArgsConstructor
public class NgrokWebServerTestConfiguration {

  @Bean
  public Tunnel ngrokTunnel() {
    final Tunnel tunnel = mock(Tunnel.class);
    when(tunnel.getPublicUrl()).thenReturn(EMPTY);
    return tunnel;
  }
}
