package md.bot.fuel.telegram.command;

import static java.util.Collections.singletonList;
import static md.bot.fuel.telegram.utils.MessageUtil.sendMessage;
import static md.bot.fuel.telegram.utils.ReplyKeyboardMarkupUtil.getFuelTypeKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class SpecificFuelInRadiusCommand implements Command {

  public static final String COMMAND = "Best fuel price";
  private static final String MESSAGE = "Select the desired type of fuel.";

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final SendMessage message = sendMessage(update, MESSAGE, getFuelTypeKeyboard());
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
