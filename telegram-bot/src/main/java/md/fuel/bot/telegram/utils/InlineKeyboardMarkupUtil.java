package md.fuel.bot.telegram.utils;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import md.fuel.bot.infrastructure.service.TranslatorService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@NoArgsConstructor(access = PRIVATE)
public class InlineKeyboardMarkupUtil {

  private static final int FIRST_FILLING_STATION = 0;

  private static final String PREVIOUS_PAGE = "PREVIOUS_PAGE";
  private static final String NEXT_PAGE = "NEXT_PAGE";
  private static final String SHOW_LOCATION = "SHOW_LOCATION";
  private static final String BACK_TO_MENU = "BACK_TO_MENU";

  private static final String SHOW_LOCATION_TEXT = "show-location.message";
  private static final String BACK_TO_MENU_TEXT = "back-to-menu.message";
  private static final String PREVIOUS_PAGE_TEXT = "⬅️";
  private static final String NEXT_PAGE_TEXT = "➡️";

  public static InlineKeyboardMarkup getInlineKeyboardForAllFillingStations(int commandId, Integer offset,
      boolean nextPageExist, TranslatorService translatorService, String userLanguage) {
    final String previousCallback = buildCallbackData(PREVIOUS_PAGE, commandId, offset);
    final String nextCallback = buildCallbackData(NEXT_PAGE, commandId, offset);
    final String locationCallback = buildCallbackData(SHOW_LOCATION, commandId, offset);

    return getSendInlineKeyboard(offset, previousCallback, nextCallback, locationCallback, nextPageExist, translatorService,
        userLanguage);
  }

  public static InlineKeyboardMarkup getInlineKeyboardForBestFuelPriceStation(int commandId, Integer offset, String fuelType,
      boolean nextPageExist, TranslatorService translatorService, String userLanguage) {
    final String previousCallback = buildCallbackData(PREVIOUS_PAGE, commandId, offset, fuelType);
    final String nextCallback = buildCallbackData(NEXT_PAGE, commandId, offset, fuelType);
    final String locationCallback = buildCallbackData(SHOW_LOCATION, commandId, offset, fuelType);

    return getSendInlineKeyboard(offset, previousCallback, nextCallback, locationCallback, nextPageExist, translatorService,
        userLanguage);
  }

  public static InlineKeyboardMarkup getInlineKeyboardForLocation(int commandId, Integer offset, String fuelType,
      TranslatorService translatorService, String userLanguage) {
    final String backToMenuCallback = buildCallbackData(BACK_TO_MENU, commandId, offset, fuelType);

    final String backToMenuText = translatorService.translate(userLanguage, BACK_TO_MENU_TEXT);
    final InlineKeyboardButton inlineKeyboardButton = buildInlineKeyboardButton(backToMenuCallback, backToMenuText);
    final List<List<InlineKeyboardButton>> rowsInline = List.of(List.of(inlineKeyboardButton));
    return new InlineKeyboardMarkup(rowsInline);
  }

  public static InlineKeyboardMarkup getSendInlineKeyboard(Integer offset, String previousCallback, String nextCallback,
      String locationCallback, boolean nextPageExist, TranslatorService translatorService, String userLanguage) {
    final List<InlineKeyboardButton> rowInlineFirst = new ArrayList<>();

    final String showLocationText = translatorService.translate(userLanguage, SHOW_LOCATION_TEXT);

    if (FIRST_FILLING_STATION < offset) {
      final InlineKeyboardButton previousPage = buildInlineKeyboardButton(previousCallback, PREVIOUS_PAGE_TEXT);
      rowInlineFirst.add(previousPage);
    }

    if (nextPageExist) {
      final InlineKeyboardButton nextPage = buildInlineKeyboardButton(nextCallback, NEXT_PAGE_TEXT);
      rowInlineFirst.add(nextPage);
    }

    final InlineKeyboardButton showLocation = buildInlineKeyboardButton(locationCallback, showLocationText);
    final List<List<InlineKeyboardButton>> rowsInline = List.of(rowInlineFirst, List.of(showLocation));
    return new InlineKeyboardMarkup(rowsInline);
  }

  private static String buildCallbackData(Object... args) {
    return Arrays.stream(args)
        .map(String::valueOf)
        .collect(Collectors.joining(SPACE));
  }

  private static InlineKeyboardButton buildInlineKeyboardButton(String callbackData, String text) {
    return InlineKeyboardButton.builder()
        .callbackData(callbackData)
        .text(text)
        .build();
  }
}