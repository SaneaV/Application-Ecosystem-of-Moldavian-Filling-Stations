package md.fuel.bot.telegram.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.configuration.RequestRateValidator;
import md.fuel.bot.telegram.exception.TelegramWrappingStrategyImpl;
import md.fuel.bot.telegram.utils.ChatInfoUtil;
import md.fuel.bot.telegram.validation.UserStatusValidatorImpl;
import md.telegram.lib.TelegramBotWebhook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@ExtendWith(SpringExtension.class)
@Import({TelegramWrappingStrategyImpl.class, ChatInfoHolder.class, UserStatusValidatorImpl.class, ChatInfoUtil.class})
@WebMvcTest(TelegramBotController.class)
public class TelegramBotControllerIT {

  private static final String BOT_PATH = "/callback/%s";

  private static final String REQUEST_BODY = """
      {
          "message": {
              "chat": {
                  "id": 12345
              },
              "from": {
                  "id": 12345
              }
          }
      }""";
  private static final String RESPONSE_BODY = "{\"method\":\"sendmessage\"}";

  @Value("${telegram.bot-token")
  private String botToken;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TelegramBotWebhook fillingStationTelegramBot;

  @MockBean
  private RequestRateValidator requestRateValidator;

  @Test
  @DisplayName("Should handle telegram request")
  void shouldHandleTelegramRequest() throws Exception {
    final BotApiMethod<Message> message = new SendMessage();

    when(fillingStationTelegramBot.onWebhookUpdateReceived(any())).thenReturn((BotApiMethod) message);

    final String uri = String.format(BOT_PATH, botToken);

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(RESPONSE_BODY));
  }
}
