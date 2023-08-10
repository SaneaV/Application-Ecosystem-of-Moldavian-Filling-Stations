package md.bot.fuel.telegram.configuration;

import com.github.alexdlaird.ngrok.protocol.Tunnel;
import lombok.RequiredArgsConstructor;
import md.bot.fuel.telegram.FuelStationTelegramBot;
import md.bot.fuel.telegram.command.DispatcherCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

@Configuration
@RequiredArgsConstructor
public class WebhookConfiguration {

    private final TelegramConfiguration telegramConfiguration;

    @Value("${telegram.webhook.internal-url}")
    private String internalUrl;

    @Value("${ngrok.enabled}")
    private String enabled;

    @Bean
    @ConditionalOnProperty(value = "ngrok.enabled", havingValue = "false")
    public SetWebhook setWebhookWithProperty() {
        System.out.println(enabled);
        return SetWebhook.builder()
                .url(telegramConfiguration.getWebhookHost())
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "ngrok.enabled", havingValue = "true")
    public SetWebhook setWebhookWithNgrok(Tunnel tunnel) {
        return SetWebhook.builder()
                .url(tunnel.getPublicUrl())
                .build();
    }

    @Bean
    public FuelStationTelegramBot fuelStationTelegramBot(SetWebhook setWebhook, DispatcherCommand dispatcherCommand,
                                                         DefaultWebhook defaultWebhook) throws TelegramApiException {
        final FuelStationTelegramBot bot = new FuelStationTelegramBot(setWebhook, telegramConfiguration.getBotToken(),
                dispatcherCommand);

        bot.setBotPath(telegramConfiguration.getBotName());
        bot.setBotUsername(telegramConfiguration.getBotName());

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);

        telegramBotsApi.registerBot(bot, setWebhook);
        return bot;
    }

    @Bean
    public DefaultWebhook defaultWebhook() {
        final DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl(internalUrl);
        return defaultWebhook;
    }
}
