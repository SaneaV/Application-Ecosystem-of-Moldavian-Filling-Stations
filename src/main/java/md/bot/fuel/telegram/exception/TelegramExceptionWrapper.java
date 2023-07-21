package md.bot.fuel.telegram.exception;

import md.bot.fuel.infrastructure.exception.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.ExceptionWrapper;
import md.bot.fuel.infrastructure.exception.ExecutionException;
import md.bot.fuel.infrastructure.exception.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class TelegramExceptionWrapper implements ExceptionWrapper {

    private static final String CHAT_ID_ATTRIBUTE = "chatId";
    private static final String UNKNOWN_ERROR = "Unknown error. Please contact bot administrator";

    @Override
    public ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception, WebRequest request) {
        return prepareErrorMessage(UNKNOWN_ERROR, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    @Override
    public ResponseEntity<BotApiMethod<?>> handleExecutionException(ExecutionException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    @Override
    public ResponseEntity<BotApiMethod<?>> handleInvalidRequestException(InvalidRequestException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    private ResponseEntity<BotApiMethod<?>> prepareErrorMessage(String errorText, WebRequest request) {
        final Long chatId = getChatId(request);
        final SendMessage errorMessage = sendMessage(chatId, errorText, getMainMenuKeyboard());
        return ResponseEntity.ok().body(errorMessage);
    }

    private Long getChatId(WebRequest request) {
        return (Long) request.getAttribute(CHAT_ID_ATTRIBUTE, SCOPE_REQUEST);
    }
}
