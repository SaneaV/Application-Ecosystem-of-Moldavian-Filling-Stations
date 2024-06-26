package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getFuelTypeKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.telegram.lib.action.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpecificFuelInRadiusCommand implements Command {

  private final ChatInfoHolder chatInfoHolder;

  public static final String COMMAND = "Best fuel price";
  private static final String MESSAGE = "Select the desired type of fuel.";

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long chatId = chatInfoHolder.getChatId();
    log.info("Display reply keyboard with available fuel types for user = {}", chatId);

    final SendMessage message = sendMessage(chatId, MESSAGE, getFuelTypeKeyboard());
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
