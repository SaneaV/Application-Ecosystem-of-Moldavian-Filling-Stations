package md.bot.fuel.infrastructure.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final HttpStatus status = NOT_FOUND;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
