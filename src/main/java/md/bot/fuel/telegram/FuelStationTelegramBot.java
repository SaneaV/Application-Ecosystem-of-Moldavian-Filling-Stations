package md.bot.fuel.telegram;

import static lombok.AccessLevel.NONE;

import lombok.Getter;
import lombok.Setter;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.telegram.command.DispatcherCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
public class FuelStationTelegramBot extends SpringWebhookBot {

  private static final String ERROR_DESCRIPTION = "An error occurred during message sending.";
  private static final String ERROR_REASON_CODE = "INTERNAL_ERROR";

  private String botPath;
  private String botUsername;
  @Getter(NONE)
  private final DispatcherCommand dispatcherCommand;

  public FuelStationTelegramBot(SetWebhook setWebhook, String botToken, DispatcherCommand dispatcherCommand) {
    super(setWebhook, botToken);
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
        throw new ExecutionException(ERROR_DESCRIPTION, ERROR_REASON_CODE);
      }
    });
    return null;
  }
}
