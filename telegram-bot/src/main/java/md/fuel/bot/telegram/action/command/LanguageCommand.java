package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getStationTypeMenuKeyboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.StationType;
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
public class LanguageCommand implements Command {

  private static final String SELECT_STATION_TYPE_MESSAGE = "select-station-type.message";
  private static final String WELCOME_MESSAGE = "welcome.message";

  public static final String RUSSIAN = "language.russian.message";
  public static final String ROMANIAN = "language.romanian.message";
  public static final String ENGLISH = "language.english.message";
  public static final String UKRAINIAN = "language.ukrainian.message";

  private static final Map<String, String> LANGUAGE_TO_TAG;

  static {
    LANGUAGE_TO_TAG = new HashMap<>();
    LANGUAGE_TO_TAG.put("Русский \uD83C\uDDF7\uD83C\uDDFA", "ru");
    LANGUAGE_TO_TAG.put("Română \uD83C\uDDF7\uD83C\uDDF4", "ro");
    LANGUAGE_TO_TAG.put("English \uD83C\uDDEC\uD83C\uDDE7", "en");
    LANGUAGE_TO_TAG.put("Українська \uD83C\uDDFA\uD83C\uDDE6", "ua");
  }

  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final String language = update.getMessage().getText();
    final String languageTag = convertMessageToTag(language);
    log.info("Update user language, userId: {}, language: {}", userId, languageTag);

    userDataFacade.updateLanguage(userId, languageTag);

    final StationType stationType = userDataFacade.getStationType(userId);

    final String messageText;
    final ReplyKeyboardMarkup keyboard;

    if (isNull(stationType)) {
      log.info("User {} has not selected station type yet, showing station type menu", userId);
      messageText = translatorService.translate(languageTag, SELECT_STATION_TYPE_MESSAGE);
      keyboard = getStationTypeMenuKeyboard(translatorService, languageTag);
    } else {
      log.info("User {} already has station type {}, showing main menu", userId, stationType);
      messageText = translatorService.translate(languageTag, WELCOME_MESSAGE);
      keyboard = getMainMenuKeyboard(translatorService, languageTag);
    }

    final SendMessage message = sendMessage(chatInfoHolder.getChatId(), messageText, keyboard);
    return singletonList(message);
  }

  private String convertMessageToTag(String message) {
    return LANGUAGE_TO_TAG.get(message);
  }

  @Override
  public List<String> getCommands() {
    return List.of(RUSSIAN, ROMANIAN, ENGLISH, UKRAINIAN);
  }
}
