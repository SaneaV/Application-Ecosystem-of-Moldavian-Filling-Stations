package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getFuelTypeKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.telegram.lib.action.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpecificFuelInRadiusCommand implements Command {

  public static final String COMMAND = "best-fuel-price.message";
  private static final String MESSAGE = "select-fuel-type.message";

  private final ChatInfoHolder chatInfoHolder;
  private final UserDataFacade userDataFacade;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long chatId = chatInfoHolder.getChatId();
    log.info("Display reply keyboard with available fuel types for user = {}", chatId);

    final String language = userDataFacade.getUserData(chatId).getLanguage();
    final String messageText = translatorService.translate(language, MESSAGE);
    final ReplyKeyboardMarkup fuelTypeKeyboard = getFuelTypeKeyboard(translatorService, language);

    final SendMessage message = sendMessage(chatId, messageText, fuelTypeKeyboard);
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
