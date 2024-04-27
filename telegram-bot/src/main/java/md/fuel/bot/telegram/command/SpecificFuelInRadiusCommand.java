package md.fuel.bot.telegram.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getFuelTypeKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpecificFuelInRadiusCommand implements Command {

  public static final String COMMAND = "Best fuel price";
  private static final String MESSAGE = "Select the desired type of fuel.";

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    log.info("Display reply keyboard with available fuel types for user = {}", update.getMessage().getFrom().getId());

    final SendMessage message = sendMessage(update, MESSAGE, getFuelTypeKeyboard());
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
