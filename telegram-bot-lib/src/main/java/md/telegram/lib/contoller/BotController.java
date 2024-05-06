package md.telegram.lib.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotController {

  @PostMapping(value = "/callback/${telegram.bot-token}")
  ResponseEntity<BotApiMethod<?>> onUpdateReceived(@RequestBody Update update);
}