package md.fuel.bot.telegram.action.command;

import static md.fuel.bot.telegram.action.callback.type.CallbackCommand.BEST_FUEL_IN_RADIUS;
import static md.fuel.bot.telegram.converter.FuelTypeNormalizer.toEnglish;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.InlineKeyboardMarkupUtil.getInlineKeyboardForBestFuelPriceStation;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.telegram.lib.action.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class BestFuelInRadiusCommand implements Command {

  private static final String MESSAGE = "navigation.message";
  private static final int FIRST_MESSAGE_OFFSET = 0;

  private static final String PETROL = "fuel.petrol.message";
  private static final String GAS = "fuel.gas.message";
  private static final String DIESEL = "fuel.diesel.message";

  public static final List<String> COMMAND = List.of(PETROL, GAS, DIESEL);

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final long chatId = chatInfoHolder.getChatId();
    log.info("Get all filling stations in radius with best fuel price for user = {}", userId);

    final String fuelType = update.getMessage().getText();
    final String translatedFuelType = toEnglish(translatorService, fuelType);
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final String language = userData.getLanguage();
    final double latitude = userData.getLatitude();
    final double longitude = userData.getLongitude();
    final double radius = userData.getRadius();
    final FillingStation fillingStation = fillingStationFacade.getBestFuelPrice(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius(), translatedFuelType, FIRST_MESSAGE_OFFSET);
    final boolean hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, FIRST_MESSAGE_OFFSET, translatedFuelType);

    final String messageText = toMessage(fillingStation, fuelType, translatorService, language);
    final SendMessage fillingStationMessage = sendMessage(chatId, messageText);
    final InlineKeyboardMarkup inlineKeyboard = getInlineKeyboardForBestFuelPriceStation(BEST_FUEL_IN_RADIUS.getCommandId(),
        FIRST_MESSAGE_OFFSET, translatedFuelType, hasNext, translatorService, language);
    fillingStationMessage.setReplyMarkup(inlineKeyboard);
    final String message = translatorService.translate(language, MESSAGE);
    final ReplyKeyboardMarkup mainMenuKeyboard = getMainMenuKeyboard(translatorService, language);
    final SendMessage backToMenu = sendMessage(chatId, message, mainMenuKeyboard);

    return List.of(fillingStationMessage, backToMenu);
  }

  @Override
  public List<String> getCommands() {
    return COMMAND;
  }
}