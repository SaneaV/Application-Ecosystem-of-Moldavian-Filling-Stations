package md.fuel.bot.telegram.utils;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static lombok.AccessLevel.PRIVATE;
import static md.fuel.bot.telegram.action.command.LanguageCommand.ENGLISH;
import static md.fuel.bot.telegram.action.command.LanguageCommand.ROMANIAN;
import static md.fuel.bot.telegram.action.command.LanguageCommand.RUSSIAN;
import static md.fuel.bot.telegram.action.command.LanguageCommand.UKRAINIAN;
import static md.fuel.bot.telegram.action.command.SelectStationTypeCommand.ELECTRIC_STATION;
import static md.fuel.bot.telegram.action.command.SelectStationTypeCommand.FUEL_STATION;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.fuel.bot.telegram.action.command.AllFillingStationInRadiusCommand;
import md.fuel.bot.telegram.action.command.BestFuelInRadiusCommand;
import md.fuel.bot.telegram.action.command.NearestFillingStationCommand;
import md.fuel.bot.telegram.action.command.PreferencesCommand;
import md.fuel.bot.telegram.action.command.SpecificFuelInRadiusCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class ReplyKeyboardMarkupUtil {

  public static String DEFAULT_LANGUAGE = "en";

  public static ReplyKeyboardMarkup getMainMenuKeyboard(TranslatorService translatorService, String language) {
    final String allFillingStationCommand = translatorService.translate(language, AllFillingStationInRadiusCommand.COMMAND);
    final String nearestFillingStationCommand = translatorService.translate(language, NearestFillingStationCommand.COMMAND);
    final String specificFuelStationCommand = translatorService.translate(language, SpecificFuelInRadiusCommand.COMMAND);
    final String preferencesCommand = translatorService.translate(language, PreferencesCommand.COMMAND);
    return getReplyKeyboardMarkup(
        asList(allFillingStationCommand, nearestFillingStationCommand, specificFuelStationCommand, preferencesCommand));
  }

  public static ReplyKeyboardMarkup getFuelTypeKeyboard(TranslatorService translatorService, String language) {
    final List<String> translatedFuelTypes = BestFuelInRadiusCommand.COMMAND.stream()
        .map(fuelType -> {
          try {
            return translatorService.translate(language, fuelType);
          } catch (Exception e) {
            log.info("Fuel type not found for translation: {}", fuelType);
          }
          return null;
        })
        .toList();
    return getReplyKeyboardMarkup(translatedFuelTypes);
  }

  public static ReplyKeyboardMarkup getLanguageMenuKeyboard(TranslatorService translatorService) {
    final String russian = translatorService.translate(DEFAULT_LANGUAGE, RUSSIAN);
    final String romanian = translatorService.translate(DEFAULT_LANGUAGE, ROMANIAN);
    final String english = translatorService.translate(DEFAULT_LANGUAGE, ENGLISH);
    final String ukrainian = translatorService.translate(DEFAULT_LANGUAGE, UKRAINIAN);
    return getReplyKeyboardMarkup(asList(russian, romanian, english, ukrainian));
  }

  public static ReplyKeyboardMarkup getStationTypeMenuKeyboard(TranslatorService translatorService, String language) {
    final String fuelStation = translatorService.translate(language, FUEL_STATION);
    final String electricStation = translatorService.translate(language, ELECTRIC_STATION);
    return getReplyKeyboardMarkup(asList(fuelStation, electricStation));
  }

  public static ReplyKeyboardMarkup getPreferencesMenuKeyboard(TranslatorService translatorService, String language) {
    final String changeLanguage = translatorService.translate(language, "change-language.message");
    final String changeStationType = translatorService.translate(language, "change-station-type.message");
    return getReplyKeyboardMarkup(asList(changeLanguage, changeStationType));
  }

  private static ReplyKeyboardMarkup getReplyKeyboardMarkup(List<String> items) {
    final List<KeyboardButton> buttons = items.stream()
        .map(KeyboardButton::new)
        .toList();

    final List<KeyboardRow> keyboardRows = buttons.stream()
        .map(button -> new KeyboardRow(singletonList(button)))
        .toList();

    return createReplyKeyBoardMarkup(keyboardRows);
  }

  private static ReplyKeyboardMarkup createReplyKeyBoardMarkup(List<KeyboardRow> keyboardRowList) {
    return ReplyKeyboardMarkup.builder()
        .resizeKeyboard(true)
        .keyboard(keyboardRowList)
        .build();
  }
}
