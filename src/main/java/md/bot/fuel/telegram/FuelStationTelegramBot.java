package md.bot.fuel.telegram;

import md.bot.fuel.infrastructure.exception.ExecutionException;
import md.bot.fuel.telegram.command.DispatcherCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class FuelStationTelegramBot extends TelegramWebhookBot {

    private static final String ERROR_DESCRIPTION = "An error occurred during message sending.";

    private final String webHookPath;
    private final String botUserName;
    private final DispatcherCommand dispatcherCommand;

    public FuelStationTelegramBot(@Value("${telegrambot.webHookPath}") String webHookPath,
                                  @Value("${telegrambot.userName}") String botUserName,
                                  @Value("${telegrambot.botToken}") String botToken,
                                  DispatcherCommand dispatcherCommand) {
        super(botToken);
        this.webHookPath = webHookPath;
        this.botUserName = botUserName;
        this.dispatcherCommand = dispatcherCommand;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        dispatcherCommand.getMessages(update).forEach(message -> {
            try {
                if (message instanceof SendMessage) {
                    execute((SendMessage) message);
                }
                if (message instanceof SendLocation) {
                    execute((SendLocation) message);
                }
            } catch (TelegramApiException e) {
                throw new ExecutionException(ERROR_DESCRIPTION);
            }
        });

        return null;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }
}
