package md.bot.fuel.telegram.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import md.bot.fuel.infrastructure.exception.ErrorWrappingStrategyFactory;
import md.bot.fuel.telegram.FuelStationTelegramBot;
import md.bot.fuel.telegram.exception.TelegramExceptionWrappingStrategy;
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

@ExtendWith(SpringExtension.class)
@Import({TelegramExceptionWrappingStrategy.class, ErrorWrappingStrategyFactory.class})
@WebMvcTest(BotController.class)
public class BotControllerIT {

  private static final String BOT_PATH = "/callback/%s";

  private static final String REQUEST_BODY = "{\n" +
      "    \"message\": {\n" +
      "        \"chat\": {\n" +
      "            \"id\": 12345\n" +
      "        }\n" +
      "    }\n" +
      "}";
  private static final String RESPONSE_BODY = "{\"method\":\"sendmessage\"}";

  @Value("${telegram.bot-token")
  private String botToken;

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private FuelStationTelegramBot fuelStationTelegramBot;

  @Test
  @DisplayName("Should handle telegram request")
  void shouldHandleTelegramRequest() throws Exception {
    final BotApiMethod message = new SendMessage();

    when(fuelStationTelegramBot.onWebhookUpdateReceived(any())).thenReturn(message);

    final String uri = String.format(BOT_PATH, botToken);

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(RESPONSE_BODY));
  }
}
