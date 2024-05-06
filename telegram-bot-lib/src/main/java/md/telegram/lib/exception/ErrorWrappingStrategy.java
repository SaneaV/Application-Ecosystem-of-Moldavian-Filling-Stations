package md.telegram.lib.exception;

import org.springframework.http.ResponseEntity;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface ErrorWrappingStrategy {

  ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception);

  ResponseEntity<BotApiMethod<?>> handleTelegramException(TelegramException exception);
}
