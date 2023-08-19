package md.fuel.bot.telegram.configuration;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.telegram.telegrambots.meta.api.methods.updates.SetWebhook.PATH;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;

@Configuration
public class SetWebhookCall {

  private final TelegramBotConfiguration telegramBotConfiguration;
  private final SetWebhook setWebhook;
  private final WebClient webClient;
  private final String setWebhookEndpoint;

  public SetWebhookCall(TelegramBotConfiguration telegramBotConfiguration, SetWebhook setWebhook, WebClient webClient,
      @Value("${telegram.set-webhook.endpoint}") String setWebhookEndpoint) {
    this.telegramBotConfiguration = telegramBotConfiguration;
    this.setWebhook = setWebhook;
    this.webClient = webClient;
    this.setWebhookEndpoint = setWebhookEndpoint;
  }

  @PostConstruct
  private void setTelegramWebhook() {
    final String setWebhookUrl = String.format(setWebhookEndpoint, telegramBotConfiguration.getBotToken(), PATH);
    webClient.post()
        .uri(setWebhookUrl)
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .header(ACCEPT, APPLICATION_JSON_VALUE)
        .bodyValue(setWebhook)
        .retrieve()
        .bodyToFlux(ApiResponse.class)
        .collectList()
        .block();
  }
}
