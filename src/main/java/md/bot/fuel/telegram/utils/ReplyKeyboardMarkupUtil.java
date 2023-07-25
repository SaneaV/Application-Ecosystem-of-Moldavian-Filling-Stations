package md.bot.fuel.telegram.utils;

import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import md.bot.fuel.telegram.command.AllInRadiusFuelStationCommand;
import md.bot.fuel.telegram.command.NearestFuelStationCommand;
import md.bot.fuel.telegram.command.SpecificFuelInRadiusCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static md.bot.fuel.telegram.command.BestFuelInRadiusCommand.DIESEL;
import static md.bot.fuel.telegram.command.BestFuelInRadiusCommand.GAS;
import static md.bot.fuel.telegram.command.BestFuelInRadiusCommand.PETROL;

@NoArgsConstructor(access = PRIVATE)
public class ReplyKeyboardMarkupUtil {

    private static final List<String> MAIN_MENU_ITEMS = new ArrayList<>();
    private static final List<String> FUEL_TYPE_MENU_ITEMS = new ArrayList<>();

    static {
        MAIN_MENU_ITEMS.addAll(asList(AllInRadiusFuelStationCommand.COMMAND, NearestFuelStationCommand.COMMAND,
                SpecificFuelInRadiusCommand.COMMAND));
        FUEL_TYPE_MENU_ITEMS.addAll(asList(PETROL, DIESEL, GAS));
    }

    public static ReplyKeyboardMarkup getMainMenuKeyboard() {
        return getReplyKeyboardMarkup(MAIN_MENU_ITEMS);
    }

    public static ReplyKeyboardMarkup getFuelTypeKeyboard() {
        return getReplyKeyboardMarkup(FUEL_TYPE_MENU_ITEMS);
    }

    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(List<String> items) {
        final List<KeyboardButton> buttons = items.stream()
                .map(KeyboardButton::new)
                .collect(toList());

        final List<KeyboardRow> keyboardRows = buttons.stream()
                .map(button -> new KeyboardRow(singletonList(button)))
                .collect(toList());

        return createReplyKeyBoardMarkup(keyboardRows);
    }

    private static ReplyKeyboardMarkup createReplyKeyBoardMarkup(List<KeyboardRow> keyboardRowList) {
        return ReplyKeyboardMarkup.builder()
                .resizeKeyboard(true)
                .keyboard(keyboardRowList)
                .build();
    }
}
