package md.bot.fuel.configuration;

import lombok.RequiredArgsConstructor;
import md.bot.fuel.telegram.FuelStationTelegramBot;
import md.bot.fuel.telegram.command.DispatcherCommand;
import md.bot.fuel.telegram.configuration.TelegramConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@TestConfiguration
@RequiredArgsConstructor
public class WebhookTestConfiguration {

    private final TelegramConfiguration telegramConfiguration;

    @Bean
    public FuelStationTelegramBot fuelStationTelegramBot(SetWebhook setWebhookInstance, DispatcherCommand dispatcherCommand) {
        final FuelStationTelegramBot fuelStationTelegramBot = new FuelStationTelegramBot(setWebhookInstance,
                telegramConfiguration.getBotToken(), dispatcherCommand);

        fuelStationTelegramBot.setBotPath(telegramConfiguration.getBotName());
        fuelStationTelegramBot.setBotUsername(telegramConfiguration.getBotName());

        return fuelStationTelegramBot;
    }
}
