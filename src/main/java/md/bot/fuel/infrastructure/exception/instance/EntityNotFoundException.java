package md.bot.fuel.infrastructure.exception.instance;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final HttpStatus status = NOT_FOUND;
    private final String reasonCode;

    public EntityNotFoundException(String message, String reasonCode) {
        super(message);
        this.reasonCode = reasonCode;
    }
}