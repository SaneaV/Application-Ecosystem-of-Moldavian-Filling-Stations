package md.bot.fuel.telegram.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TelegramConfiguration {

    private final String webhookHost;
    private final String botName;
    private final String botToken;

    public TelegramConfiguration(@Value("${telegram.webhook.host}") String webhookHost,
                                 @Value("${telegram.bot-name}") String botName,
                                 @Value("${telegram.bot-token}") String botToken) {
        this.webhookHost=webhookHost;
        this.botName = botName;
        this.botToken = botToken;
    }
}