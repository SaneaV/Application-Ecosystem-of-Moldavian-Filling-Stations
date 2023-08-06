package md.bot.fuel.telegram.exception;

import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;
import md.bot.fuel.infrastructure.exception.ErrorWrappingStrategy;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class TelegramExceptionWrappingStrategy implements ErrorWrappingStrategy {

    private static final String CHAT_ID_ATTRIBUTE = "chatId";
    private static final String UNKNOWN_ERROR = "Unknown error. Please contact bot administrator";
    private static final String CLIENT = "TELEGRAM";

    @Override
    public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
        return prepareErrorMessage(UNKNOWN_ERROR, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    @Override
    public ResponseEntity<ErrorDescriptionResponse> handleExecutionException(ExecutionException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    @Override
    public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    // TODO: Remove this handle on module separation
    @Override
    public ResponseEntity<ErrorDescriptionResponse> handleBindException(BindException exception, WebRequest request) {
        return prepareErrorMessage(exception.getMessage(), request);
    }

    @Override
    public String getClient() {
        return CLIENT;
    }

    private ResponseEntity<ErrorDescriptionResponse> prepareErrorMessage(String errorText, WebRequest request) {
        final Long chatId = getChatId(request);
        final TelegramErrorDescription telegramErrorDescription = new TelegramErrorDescription(chatId, errorText, getMainMenuKeyboard());
        return ResponseEntity.ok().body(telegramErrorDescription);
    }

    private Long getChatId(WebRequest request) {
        return (Long) request.getAttribute(CHAT_ID_ATTRIBUTE, SCOPE_REQUEST);
    }
}
