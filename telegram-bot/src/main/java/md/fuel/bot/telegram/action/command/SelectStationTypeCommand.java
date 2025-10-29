package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.domain.StationType.ELECTRIC;
import static md.fuel.bot.domain.StationType.FUEL;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.Arrays;
import java.util.List;
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
public class SelectStationTypeCommand implements Command {

  private static final String MESSAGE_CODE = "welcome.message";

  public static final String FUEL_STATION = "station-type.fuel.message";
  public static final String ELECTRIC_STATION = "station-type.electric.message";

  private static final List<String> LANGUAGES = Arrays.asList("ru", "ro", "en", "ua");

  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final String message = update.getMessage().getText();
    final String language = userDataFacade.getLanguage(userId);
    final StationType stationType = convertMessageToType(message);
    log.info("Update user station type, userId: {}, stationType: {}", userId, stationType);

    userDataFacade.updateStationType(userId, stationType);

    final String messageText = translatorService.translate(language, MESSAGE_CODE);

    final ReplyKeyboardMarkup mainMenuKeyboard = getMainMenuKeyboard(translatorService, language);
    final SendMessage sendMessage = sendMessage(chatInfoHolder.getChatId(), messageText, mainMenuKeyboard);
    return singletonList(sendMessage);
  }

  private StationType convertMessageToType(String message) {
    for (String lang : LANGUAGES) {
      final String fuelTranslation = translatorService.translate(lang, FUEL_STATION);
      if (message.equals(fuelTranslation)) {
        return FUEL;
      }

      final String electricTranslation = translatorService.translate(lang, ELECTRIC_STATION);
      if (message.equals(electricTranslation)) {
        return ELECTRIC;
      }
    }

    log.warn("Unknown station type message: {}, defaulting to FUEL", message);
    return FUEL;
  }

  @Override
  public List<String> getCommands() {
    return List.of(FUEL_STATION, ELECTRIC_STATION);
  }
}