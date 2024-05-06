package md.telegram.lib;

import lombok.extern.slf4j.Slf4j;
import md.telegram.lib.action.ActionHandler;
import md.telegram.lib.configuration.TelegramBotConfiguration;
import md.telegram.lib.exception.TelegramException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Component
public class TelegramBotWebhook extends SpringWebhookBot {

  private static final String ERROR_DESCRIPTION = "An error occurred during message sending.";

  private final TelegramBotConfiguration telegramBotConfiguration;
  private final ActionHandler actionHandler;

  public TelegramBotWebhook(SetWebhook setWebhook, ActionHandler actionHandler,
      TelegramBotConfiguration telegramBotConfiguration) {
    super(setWebhook, telegramBotConfiguration.getBotToken());
    this.actionHandler = actionHandler;
    this.telegramBotConfiguration = telegramBotConfiguration;
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    actionHandler.execute(update).forEach(message -> {
      try {
        if (message instanceof BotApiMethod<?> m) {
          execute(m);
        }
      } catch (TelegramApiException e) {
        log.error("There was an error sending the message");
        throw new TelegramException(ERROR_DESCRIPTION);
      }
    });
    return null;
  }

  @Override
  public String getBotPath() {
    return telegramBotConfiguration.getWebhook();
  }

  @Override
  public String getBotUsername() {
    return telegramBotConfiguration.getBotName();
  }
}
