package md.fuel.bot.telegram;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.fuel.bot.telegram.action.ActionHandler;
import md.fuel.bot.telegram.configuration.TelegramBotConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FillingStationTelegramBotTest {

  @Nested
  @ExtendWith(MockitoExtension.class)
  @Import({FillingStationTelegramBot.class})
  class FillingStationTelegramBotTestWithInjection {

    @Mock
    private TelegramBotConfiguration telegramBotConfiguration;
    @Mock
    private ActionHandler actionHandler;
    @Spy
    @InjectMocks
    private FillingStationTelegramBot fillingStationTelegramBot;

    @Test
    @DisplayName("Should execute send message on WebhookUpdateReceived")
    void shouldExecuteSendMessageOnWebhookUpdateReceived() throws TelegramApiException {
      final Update update = mock(Update.class);
      final Message message = mock(Message.class);
      final SendMessage sendMessage = new SendMessage();

      when(actionHandler.execute(update)).thenReturn(singletonList(sendMessage));
      doReturn(message).when(fillingStationTelegramBot).execute(any(SendMessage.class));

      final BotApiMethod<?> botApiMethod = fillingStationTelegramBot.onWebhookUpdateReceived(update);

      verify(actionHandler).execute(any());

      assertThat(botApiMethod).isNull();
    }

    @Test
    @DisplayName("Should execute send location on WebhookUpdateReceived")
    void shouldExecuteSendLocationOnWebhookUpdateReceived() throws TelegramApiException {
      final Update update = mock(Update.class);
      final Message message = mock(Message.class);
      final SendLocation sendLocation = new SendLocation();

      when(actionHandler.execute(update)).thenReturn(singletonList(sendLocation));
      doReturn(message).when(fillingStationTelegramBot).execute(any(SendLocation.class));

      final BotApiMethod<?> botApiMethod = fillingStationTelegramBot.onWebhookUpdateReceived(update);

      verify(actionHandler).execute(any());

      assertThat(botApiMethod).isNull();
    }

    @Test
    @DisplayName("Should throw ExecutionException on WebhookUpdateReceived")
    void shouldThrowExecutionExceptionOnWebhookUpdateReceived() throws TelegramApiException {
      final Update update = mock(Update.class);
      final SendLocation sendLocation = new SendLocation();

      when(actionHandler.execute(update)).thenReturn(singletonList(sendLocation));
      doThrow(TelegramApiException.class).when(fillingStationTelegramBot).execute(any(SendLocation.class));

      assertThatThrownBy(() -> fillingStationTelegramBot.onWebhookUpdateReceived(update))
          .isInstanceOf(InfrastructureException.class);

      verify(actionHandler).execute(any());
    }
  }

  @Nested
  class FillingStationTelegramBotTestWithoutInjection {

    private static final String WEBHOOK = "Webhook host";
    private static final String BOT_NAME = "Bot name";
    private static final String BOT_TOKEN = "Bot token";

    private final FillingStationTelegramBot fillingStationTelegramBot;
    private final TelegramBotConfiguration telegramBotConfiguration;

    FillingStationTelegramBotTestWithoutInjection() {
      final SetWebhook setWebhook = mock(SetWebhook.class);
      final ActionHandler actionHandler = mock(ActionHandler.class);
      this.telegramBotConfiguration = new TelegramBotConfiguration();
      telegramBotConfiguration.setBotName(BOT_NAME);
      telegramBotConfiguration.setBotToken(BOT_TOKEN);
      telegramBotConfiguration.setWebhook(WEBHOOK);
      this.fillingStationTelegramBot = new FillingStationTelegramBot(setWebhook, actionHandler, telegramBotConfiguration);
    }

    @Test
    @DisplayName("Should return null bot name")
    void shouldReturnBotUsername() {
      assertThat(fillingStationTelegramBot.getBotUsername()).isEqualTo(telegramBotConfiguration.getBotName());
    }

    @Test
    @DisplayName("Should return null bot path")
    void shouldReturnBotPath() {
      assertThat(fillingStationTelegramBot.getBotPath()).isEqualTo(telegramBotConfiguration.getWebhook());
    }
  }
}
