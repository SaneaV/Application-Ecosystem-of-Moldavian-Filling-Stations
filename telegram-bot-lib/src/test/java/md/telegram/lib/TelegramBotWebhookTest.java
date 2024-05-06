package md.telegram.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import md.telegram.lib.action.ActionHandler;
import md.telegram.lib.configuration.TelegramBotConfiguration;
import md.telegram.lib.exception.TelegramException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotWebhookTest {

  @Nested
  @ExtendWith(MockitoExtension.class)
  @Import({TelegramApiException.class})
  class telegramBotWebhookTestWithInjection {

    @Mock
    private TelegramBotConfiguration telegramBotConfiguration;
    @Mock
    private ActionHandler actionHandler;
    @Spy
    @InjectMocks
    private TelegramBotWebhook telegramBotWebhook;

    @Test
    @DisplayName("Should execute send message on WebhookUpdateReceived")
    void shouldExecuteSendMessageOnWebhookUpdateReceived() throws TelegramApiException {
      final Update update = mock(Update.class);
      final Message message = mock(Message.class);

      when(actionHandler.execute(update)).thenAnswer(invocation -> {
        SendMessage sendMessage = new SendMessage();
        return List.of((PartialBotApiMethod<?>) sendMessage);
      });
      doReturn(message).when(telegramBotWebhook).execute(ArgumentMatchers.any(SendMessage.class));

      final BotApiMethod<?> botApiMethod = telegramBotWebhook.onWebhookUpdateReceived(update);

      verify(actionHandler).execute(ArgumentMatchers.any());

      assertThat(botApiMethod).isNull();
    }

    @Test
    @DisplayName("Should throw TelegramException on WebhookUpdateReceived")
    void shouldThrowTelegramExceptionOnWebhookUpdateReceived() throws TelegramApiException {
      final Update update = mock(Update.class);
      final SendLocation sendLocation = new SendLocation();

      when(actionHandler.execute(update)).thenAnswer(invocation -> List.of((PartialBotApiMethod<?>) sendLocation));
      doThrow(TelegramApiException.class).when(telegramBotWebhook).execute(ArgumentMatchers.any(SendLocation.class));

      assertThatThrownBy(() -> telegramBotWebhook.onWebhookUpdateReceived(update)).isInstanceOf(TelegramException.class);

      verify(actionHandler).execute(ArgumentMatchers.any());
    }
  }

  @Nested
  class TelegramBotWebhookTestWithoutInjection {

    private static final String WEBHOOK = "Webhook host";
    private static final String BOT_NAME = "Bot name";
    private static final String BOT_TOKEN = "Bot token";

    private final TelegramBotWebhook telegramBotWebhook;
    private final TelegramBotConfiguration telegramBotConfiguration;

    TelegramBotWebhookTestWithoutInjection() {
      final SetWebhook setWebhook = mock(SetWebhook.class);
      final ActionHandler actionHandler = mock(ActionHandler.class);
      this.telegramBotConfiguration = new TelegramBotConfiguration();
      telegramBotConfiguration.setBotName(BOT_NAME);
      telegramBotConfiguration.setBotToken(BOT_TOKEN);
      telegramBotConfiguration.setWebhook(WEBHOOK);
      this.telegramBotWebhook = new TelegramBotWebhook(setWebhook, actionHandler, telegramBotConfiguration);
    }

    @Test
    @DisplayName("Should return null bot name")
    void shouldReturnBotUsername() {
      assertThat(telegramBotWebhook.getBotUsername()).isEqualTo(telegramBotConfiguration.getBotName());
    }

    @Test
    @DisplayName("Should return null bot path")
    void shouldReturnBotPath() {
      assertThat(telegramBotWebhook.getBotPath()).isEqualTo(telegramBotConfiguration.getWebhook());
    }
  }
}
