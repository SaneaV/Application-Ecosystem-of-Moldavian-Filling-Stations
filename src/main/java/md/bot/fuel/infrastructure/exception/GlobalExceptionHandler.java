package md.bot.fuel.infrastructure.exception;

import lombok.RequiredArgsConstructor;
import md.bot.fuel.infrastructure.exception.instance.EntityNotFoundException;
import md.bot.fuel.infrastructure.exception.instance.ExecutionException;
import md.bot.fuel.infrastructure.exception.instance.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String CLIENT_ATTRIBUTE = "CLIENT";

    private final ErrorWrappingStrategyFactory errorWrappingStrategyFactory;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDescriptionResponse> handleRuntimeException(RuntimeException exception, WebRequest request) {
        final String client = getClient(request);
        return errorWrappingStrategyFactory.getErrorWrappingStrategy(client).handleRuntimeException(exception, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDescriptionResponse> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        final String client = getClient(request);
        return errorWrappingStrategyFactory.getErrorWrappingStrategy(client).handleEntityNotFoundException(exception, request);
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<ErrorDescriptionResponse> handleExecutionException(ExecutionException exception, WebRequest request) {
        final String client = getClient(request);
        return errorWrappingStrategyFactory.getErrorWrappingStrategy(client).handleExecutionException(exception, request);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorDescriptionResponse> handleInvalidRequestException(InvalidRequestException exception, WebRequest request) {
        final String client = getClient(request);
        return errorWrappingStrategyFactory.getErrorWrappingStrategy(client).handleInvalidRequestException(exception, request);
    }

    private String getClient(WebRequest request) {
        return (String) request.getAttribute(CLIENT_ATTRIBUTE, SCOPE_REQUEST);
    }
}
