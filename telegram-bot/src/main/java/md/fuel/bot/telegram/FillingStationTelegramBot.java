package md.fuel.bot.telegram;

import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.fuel.bot.telegram.action.ActionHandler;
import md.fuel.bot.telegram.configuration.TelegramBotConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Component
public class FillingStationTelegramBot extends SpringWebhookBot {

  private static final String ERROR_DESCRIPTION = "An error occurred during message sending.";

  private final TelegramBotConfiguration telegramBotConfiguration;
  private final ActionHandler actionHandler;

  public FillingStationTelegramBot(SetWebhook setWebhook, ActionHandler actionHandler,
      TelegramBotConfiguration telegramBotConfiguration) {
    super(setWebhook, telegramBotConfiguration.getBotToken());
    this.actionHandler = actionHandler;
    this.telegramBotConfiguration = telegramBotConfiguration;
  }

  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    actionHandler.execute(update).forEach(message -> {
      try {
        if (message instanceof SendMessage m) {
          execute(m);
        }
        if (message instanceof SendLocation m) {
          execute(m);
        }
        if (message instanceof EditMessageText m) {
          execute(m);
        }
        if (message instanceof DeleteMessage m) {
          execute(m);
        }
      } catch (TelegramApiException e) {
        log.error("There was an error sending the message");
        throw new InfrastructureException(ERROR_DESCRIPTION);
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
