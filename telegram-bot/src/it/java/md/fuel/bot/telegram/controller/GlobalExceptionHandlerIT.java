package md.fuel.bot.telegram.controller;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.fuel.bot.telegram.FillingStationTelegramBot;
import md.fuel.bot.telegram.command.BestFuelInRadiusCommand;
import md.fuel.bot.telegram.exception.TelegramExceptionWrappingStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@Import({TelegramExceptionWrappingStrategy.class})
@WebMvcTest(BotController.class)
public class GlobalExceptionHandlerIT {

  private static final String BOT_PATH = "/callback/%s";
  private static final String ERROR_MESSAGE = "Standard error message";
  private static final String STANDARD_RUNTIME_EXCEPTION_MESSAGE = "Unknown error. Please contact bot administrator";

  private static final String REQUEST_BODY = """
      {
          "message": {
              "chat": {
                  "id": 12345
              }
          }
      }""";
  private static final String ERROR_RESPONSE = """
      {
          "chat_id": "12345",
          "text": "%s",
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

  @Value("${telegram.bot-token")
  private String botToken;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FillingStationTelegramBot fillingStationTelegramBot;

  @ParameterizedTest
  @MethodSource("getExceptions")
  @DisplayName("Should handle and wrap exceptions")
  void shouldHandleAndWrapExceptions(RuntimeException exception, String exceptionMessage) throws Exception {
    BestFuelInRadiusCommand.COMMAND = emptyList();
    when(fillingStationTelegramBot.onWebhookUpdateReceived(any())).thenThrow(exception);

    final String uri = String.format(BOT_PATH, botToken);

    mockMvc.perform(post(uri)
            .content(REQUEST_BODY)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(String.format(ERROR_RESPONSE, exceptionMessage)));
  }

  private static Stream<Arguments> getExceptions() {
    return Stream.of(
        Arguments.of(new RuntimeException(ERROR_MESSAGE), STANDARD_RUNTIME_EXCEPTION_MESSAGE),
        Arguments.of(new EntityNotFoundException(ERROR_MESSAGE), ERROR_MESSAGE),
        Arguments.of(new InfrastructureException(ERROR_MESSAGE), ERROR_MESSAGE)
    );
  }
}
