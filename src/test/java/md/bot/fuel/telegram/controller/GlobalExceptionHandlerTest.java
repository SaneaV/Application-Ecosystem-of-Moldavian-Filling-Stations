package md.bot.fuel.telegram.controller;

import java.util.stream.Stream;
import md.bot.fuel.infrastructure.exception.ErrorWrappingStrategyFactory;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import md.bot.fuel.telegram.FuelStationTelegramBot;
import md.bot.fuel.telegram.exception.TelegramExceptionWrappingStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import({TelegramExceptionWrappingStrategy.class, ErrorWrappingStrategyFactory.class})
@WebMvcTest(BotController.class)
public class GlobalExceptionHandlerTest {

    private static final String BOT_PATH = "/";
    private static final String ERROR_MESSAGE = "Standard error message";
    private static final String ERROR_REASON_CODE = "Standard reason code";
    private static final String STANDARD_RUNTIME_EXCEPTION_MESSAGE = "Unknown error. Please contact bot administrator";

    private static final String REQUEST_BODY = "{\n" +
            "    \"message\": {\n" +
            "        \"chat\": {\n" +
            "            \"id\": 12345\n" +
            "        }\n" +
            "    }\n" +
            "}";
    private static final String ERROR_RESPONSE = "{\n" +
            "    \"chat_id\": 12345,\n" +
            "    \"text\": \"%s\",\n" +
            "    \"reply_markup\": {\n" +
            "        \"keyboard\": [\n" +
            "            [\n" +
            "                {\n" +
            "                    \"text\": \"All fuel stations\"\n" +
            "                }\n" +
            "            ],\n" +
            "            [\n" +
            "                {\n" +
            "                    \"text\": \"Nearest fuel station\"\n" +
            "                }\n" +
            "            ],\n" +
            "            [\n" +
            "                {\n" +
            "                    \"text\": \"Best fuel price\"\n" +
            "                }\n" +
            "            ]\n" +
            "        ],\n" +
            "        \"resize_keyboard\": true\n" +
            "    },\n" +
            "    \"method\": \"sendmessage\"\n" +
            "}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FuelStationTelegramBot fuelStationTelegramBot;

    @ParameterizedTest
    @MethodSource("getExceptions")
    @DisplayName("Should handle and wrap exceptions")
    void shouldHandleAndWrapExceptions(RuntimeException exception, String exceptionMessage) throws Exception {
        when(fuelStationTelegramBot.onWebhookUpdateReceived(any())).thenThrow(exception);

        mockMvc.perform(MockMvcRequestBuilders.post(BOT_PATH)
                        .content(REQUEST_BODY)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format(ERROR_RESPONSE, exceptionMessage)));
    }

    private static Stream<Arguments> getExceptions() {
        return Stream.of(
                Arguments.of(new RuntimeException(ERROR_MESSAGE), STANDARD_RUNTIME_EXCEPTION_MESSAGE),
                Arguments.of(new EntityNotFoundException(ERROR_MESSAGE, ERROR_REASON_CODE), ERROR_MESSAGE),
                Arguments.of(new ExecutionException(ERROR_MESSAGE, ERROR_REASON_CODE), ERROR_MESSAGE),
                Arguments.of(new InvalidRequestException(ERROR_MESSAGE, ERROR_REASON_CODE), ERROR_MESSAGE)
        );
    }
}
