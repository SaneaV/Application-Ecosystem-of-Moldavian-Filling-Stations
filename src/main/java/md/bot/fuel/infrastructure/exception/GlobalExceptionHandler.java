package md.bot.fuel.infrastructure.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionWrapper exceptionWrapper;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception, WebRequest request) {
        return exceptionWrapper.handleRuntimeException(exception, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest request) {
        return exceptionWrapper.handleEntityNotFoundException(exception, request);
    }

    @ExceptionHandler(ExecutionException.class)
    public ResponseEntity<?> handleExecutionException(ExecutionException exception, WebRequest request) {
        return exceptionWrapper.handleExecutionException(exception, request);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException exception, WebRequest request) {
        return exceptionWrapper.handleInvalidRequestException(exception, request);
    }
}
