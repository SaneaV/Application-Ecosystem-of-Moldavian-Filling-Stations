package md.bot.fuel.telegram;

import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.telegram.command.DispatcherCommand;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FuelStationTelegramBotTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    @Import({FuelStationTelegramBot.class})
    class FuelStationTelegramBotTestWithInjection {

        @Mock
        private DispatcherCommand dispatcherCommand;
        @Spy
        @InjectMocks
        private FuelStationTelegramBot fuelStationTelegramBot;

        @Test
        @DisplayName("Should execute send message on WebhookUpdateReceived")
        void shouldExecuteSendMessageOnWebhookUpdateReceived() throws TelegramApiException {
            final Update update = mock(Update.class);
            final Message message = mock(Message.class);
            final SendMessage sendMessage = new SendMessage();

            when(dispatcherCommand.getMessages(update)).thenReturn(singletonList(sendMessage));
            doReturn(message).when(fuelStationTelegramBot).execute(any(SendMessage.class));

            final BotApiMethod<?> botApiMethod = fuelStationTelegramBot.onWebhookUpdateReceived(update);

            verify(dispatcherCommand).getMessages(any());

            assertThat(botApiMethod).isNull();
        }

        @Test
        @DisplayName("Should execute send location on WebhookUpdateReceived")
        void shouldExecuteSendLocationOnWebhookUpdateReceived() throws TelegramApiException {
            final Update update = mock(Update.class);
            final Message message = mock(Message.class);
            final SendLocation sendLocation = new SendLocation();

            when(dispatcherCommand.getMessages(update)).thenReturn(singletonList(sendLocation));
            doReturn(message).when(fuelStationTelegramBot).execute(any(SendLocation.class));

            final BotApiMethod<?> botApiMethod = fuelStationTelegramBot.onWebhookUpdateReceived(update);

            verify(dispatcherCommand).getMessages(any());

            assertThat(botApiMethod).isNull();
        }

        @Test
        @DisplayName("Should throw ExecutionException on WebhookUpdateReceived")
        void shouldThrowExecutionExceptionOnWebhookUpdateReceived() throws TelegramApiException {
            final Update update = mock(Update.class);
            final SendLocation sendLocation = new SendLocation();

            when(dispatcherCommand.getMessages(update)).thenReturn(singletonList(sendLocation));
            doThrow(TelegramApiException.class).when(fuelStationTelegramBot).execute(any(SendLocation.class));

            assertThatThrownBy(() -> fuelStationTelegramBot.onWebhookUpdateReceived(update))
                    .isInstanceOf(ExecutionException.class);

            verify(dispatcherCommand).getMessages(any());
        }
    }

    @Nested
    class FuelStationTelegramBotTestWithoutInjection {

        private static final String WEB_HOOK_PATH = "https://www.google.com";
        private static final String BOT_NAME = "bot name";
        private static final String BOT_TOKEN = "bot_token";

        private final FuelStationTelegramBot fuelStationTelegramBot;

        FuelStationTelegramBotTestWithoutInjection() {
            DispatcherCommand dispatcherCommand = mock(DispatcherCommand.class);
            this.fuelStationTelegramBot = new FuelStationTelegramBot(WEB_HOOK_PATH, BOT_NAME, BOT_TOKEN, dispatcherCommand);
        }

        @Test
        @DisplayName("Should return bot username")
        void shouldReturnBotUsername() {
            assertThat(fuelStationTelegramBot.getBotUsername()).isEqualTo(BOT_NAME);
        }

        @Test
        @DisplayName("Should return bot path")
        void shouldReturnBotPath() {
            assertThat(fuelStationTelegramBot.getBotPath()).isEqualTo(WEB_HOOK_PATH);
        }
    }
}
