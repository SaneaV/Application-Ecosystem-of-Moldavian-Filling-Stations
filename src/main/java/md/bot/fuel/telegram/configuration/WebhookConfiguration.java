package md.bot.fuel.telegram.configuration;

import lombok.RequiredArgsConstructor;
import md.bot.fuel.telegram.FuelStationTelegramBot;
import md.bot.fuel.telegram.command.DispatcherCommand;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder()
                .url(telegramConfiguration.getWebhookHost())
                .build();
    }

    @Bean
    public FuelStationTelegramBot fuelStationTelegramBot(SetWebhook setWebhookInstance, DispatcherCommand dispatcherCommand,
                                                         DefaultWebhook defaultWebhook) throws TelegramApiException {
        final FuelStationTelegramBot fuelStationTelegramBot = new FuelStationTelegramBot(setWebhookInstance,
                telegramConfiguration.getBotToken(), dispatcherCommand);

        fuelStationTelegramBot.setBotPath(telegramConfiguration.getBotName());
        fuelStationTelegramBot.setBotUsername(telegramConfiguration.getBotName());

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);

        telegramBotsApi.registerBot(fuelStationTelegramBot, setWebhookInstance);
        return fuelStationTelegramBot;
    }

    @Bean
    public DefaultWebhook defaultWebhook() {
        final DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl(internalUrl);
        return defaultWebhook;
    }
}
