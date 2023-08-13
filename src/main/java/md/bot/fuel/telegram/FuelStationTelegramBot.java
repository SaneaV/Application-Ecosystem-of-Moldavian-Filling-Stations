package md.bot.fuel.telegram;

import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.telegram.command.DispatcherCommand;
import md.bot.fuel.telegram.configuration.TelegramBotConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Component
public class FuelStationTelegramBot extends SpringWebhookBot {

  private static final String ERROR_DESCRIPTION = "An error occurred during message sending.";
  private static final String ERROR_REASON_CODE = "INTERNAL_ERROR";

  private final TelegramBotConfiguration telegramBotConfiguration;
  private final DispatcherCommand dispatcherCommand;

  public FuelStationTelegramBot(SetWebhook setWebhook, DispatcherCommand dispatcherCommand,
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
        throw new ExecutionException(ERROR_DESCRIPTION, ERROR_REASON_CODE);
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
