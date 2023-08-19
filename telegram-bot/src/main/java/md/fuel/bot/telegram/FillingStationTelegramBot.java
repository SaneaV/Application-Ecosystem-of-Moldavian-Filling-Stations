package md.fuel.bot.telegram;

import md.fuel.bot.infrastructure.exception.model.ExecutionException;
import md.fuel.bot.telegram.command.DispatcherCommand;
import md.fuel.bot.telegram.configuration.TelegramBotConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Component
public class FillingStationTelegramBot extends SpringWebhookBot {

  private static final String ERROR_DESCRIPTION = "An error occurred during message sending.";

  private final TelegramBotConfiguration telegramBotConfiguration;
  private final DispatcherCommand dispatcherCommand;

  public FillingStationTelegramBot(SetWebhook setWebhook, DispatcherCommand dispatcherCommand,
      TelegramBotConfiguration telegramBotConfiguration) {
    super(setWebhook, telegramBotConfiguration.getBotToken());
    this.dispatcherCommand = dispatcherCommand;
    this.telegramBotConfiguration = telegramBotConfiguration;
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
    return telegramBotConfiguration.getWebhookHost();
  }

  @Override
  public String getBotUsername() {
    return telegramBotConfiguration.getBotName();
  }
}
