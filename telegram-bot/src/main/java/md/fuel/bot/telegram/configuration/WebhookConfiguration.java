package md.fuel.bot.telegram.configuration;

import com.github.alexdlaird.ngrok.protocol.Tunnel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@RequiredArgsConstructor
public class WebhookConfiguration {

  private static final String SET_WEBHOOK_URL = "%s/callback/%s";

  private final TelegramBotConfiguration telegramBotConfiguration;

  @Bean
  @ConditionalOnProperty(value = "ngrok.enabled", havingValue = "false")
  public SetWebhook setWebhookWithProperty() {
    return buildSetWebhook(telegramBotConfiguration.getWebhook());
  }

  @Bean
  @ConditionalOnProperty(value = "ngrok.enabled", havingValue = "true")
  public SetWebhook setWebhookWithNgrok(Tunnel tunnel) {
    return buildSetWebhook(tunnel.getPublicUrl());
  }

  private SetWebhook buildSetWebhook(String webhook) {
    final String webhookUrl = String.format(SET_WEBHOOK_URL, webhook, telegramBotConfiguration.getBotToken());
    return SetWebhook.builder()
        .url(webhookUrl)
        .build();
  }
}
