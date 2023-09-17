package md.fuel.bot.telegram.controller;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import md.fuel.bot.infrastructure.configuration.CaffeineConfiguration;
import md.fuel.bot.telegram.FillingStationTelegramBot;
import md.fuel.bot.telegram.command.BestFuelInRadiusCommand;
import md.fuel.bot.telegram.configuration.RequestRateValidator;
import md.fuel.bot.telegram.exception.TelegramExceptionWrappingStrategy;
import org.junit.jupiter.api.BeforeAll;
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
@Import({TelegramExceptionWrappingStrategy.class, RequestRateValidator.class, CaffeineConfiguration.class})
@WebMvcTest(value = BotController.class, properties = {"telegram.bot.requests-per-second=1",
    "telegram.bot.requests-time-reset=5"})
public class RequestRateValidatorIT {

  private static final String BOT_PATH = "/callback/%s";

  private static final String REQUEST_BODY = """
      {
          "message": {
              "chat": {
                  "id": 12345
              }
          }
      }""";
  private static final String ERROR_RESPONSE_BODY = """
      {
          "chat_id": "12345",
          "text": "You have made too many requests lately. Wait a couple of seconds and try again.",
          "reply_markup": {
              "keyboard": [
                  [
                      {
                          "text": "All filling stations"
                      }
                  ],
                  [
                      {
                          "text": "Nearest filling station"
                      }
                  ],
                  [
                      {
                          "text": "Best fuel price"
                      }
                  ]
              ],
              "resize_keyboard": true
          },
          "method": "sendmessage"
      }""";
  private static final String VALID_RESPONSE_BODY = "{\"method\":\"sendmessage\"}";

  @Value("${telegram.bot-token")
  private String botToken;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FillingStationTelegramBot fillingStationTelegramBot;

  @Autowired
  private RequestRateValidator requestRateValidator;

  @BeforeAll
  static void setCommand() {
    BestFuelInRadiusCommand.COMMAND = emptyList();
  }

  @Test
  @DisplayName("Should handle too many requests")
  void shouldHandleTooManyRequests() throws Exception {
    final BotApiMethod<Message> message = new SendMessage();

    when(fillingStationTelegramBot.onWebhookUpdateReceived(any())).thenReturn((BotApiMethod) message);

    final String uri = String.format(BOT_PATH, botToken);

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(VALID_RESPONSE_BODY));

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(ERROR_RESPONSE_BODY));
  }

  @Test
  @DisplayName("Should reset requests count")
  void shouldResetRequestsCount() throws Exception {
    final BotApiMethod<Message> message = new SendMessage();

    when(fillingStationTelegramBot.onWebhookUpdateReceived(any())).thenReturn((BotApiMethod) message);

    final String uri = String.format(BOT_PATH, botToken);

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(VALID_RESPONSE_BODY));

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(ERROR_RESPONSE_BODY));

    Thread.sleep(5000);

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(VALID_RESPONSE_BODY));
  }
}