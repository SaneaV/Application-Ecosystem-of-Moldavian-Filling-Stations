package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getPreferencesMenuKeyboard;

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
public class PreferencesCommand implements Command {

  public static final String COMMAND = "preferences.message";
  private static final String MESSAGE = "preferences.message";

  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);
    log.info("Show preferences menu for user = {}", userId);

    final String messageText = translatorService.translate(language, MESSAGE);
    final ReplyKeyboardMarkup preferencesMenuKeyboard = getPreferencesMenuKeyboard(translatorService, language);

    final SendMessage message = sendMessage(chatInfoHolder.getChatId(), messageText, preferencesMenuKeyboard);
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}