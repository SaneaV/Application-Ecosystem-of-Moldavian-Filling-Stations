package md.bot.fuel.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class InvalidRequestException extends RuntimeException {

    private final HttpStatus status = BAD_REQUEST;

    public InvalidRequestException(String message) {
        super(message);
    }
}
