package md.bot.fuel.infrastructure.exception.instance;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class ExecutionException extends RuntimeException {

    private final HttpStatus status = BAD_REQUEST;
    private final String reasonCode;

    public ExecutionException(String message, String reasonCode) {
        super(message);
        this.reasonCode = reasonCode;
    }
}
