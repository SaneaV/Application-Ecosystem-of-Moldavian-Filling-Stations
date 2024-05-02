package md.fuel.bot.telegram.action.callback;

import static md.fuel.bot.telegram.action.callback.type.CallbackCommand.ALL_FILLING_STATIONS_IN_RADIUS;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.InlineKeyboardMarkupUtil.getInlineKeyboardForAllFillingStations;
import static md.fuel.bot.telegram.utils.MessageUtil.editMessageText;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.dto.UserDataDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class NextFillingStationCallback implements Callback {

  private static final String CALLBACK_QUERY = "NEXT_PAGE";
  private static final String OFFSET = "OFFSET";
  private static final String COMMAND = "COMMAND";
  private static final String FUEL_TYPE = "FUEL_TYPE";

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final CallbackHolder callbackHolder;

  @Override
  public List<? super PartialBotApiMethod<?>> execute(CallbackQuery callbackQuery) {
    final long userId = chatInfoHolder.getUserId();
    final int nextOffset = Integer.parseInt(callbackHolder.getCallbackDataBy(OFFSET)) + 1;
    log.info("Get all filling stations (next page) in radius for user = {} and offset = {}", userId, nextOffset);

    final UserDataDto userData = userDataFacade.getUserData(userId);
    final double latitude = userData.getLatitude();
    final double longitude = userData.getLongitude();
    final double radius = userData.getRadius();
    final int command = Integer.parseInt(callbackHolder.getCallbackDataBy(COMMAND));
    final String fuelType = callbackHolder.getCallbackDataBy(FUEL_TYPE);

    final FillingStation fillingStation;
    final boolean hasNext;
    final String messageText;

    if (ALL_FILLING_STATIONS_IN_RADIUS.getCommandId() == command) {
      fillingStation = fillingStationFacade.getAllFillingStations(latitude, longitude, radius, nextOffset);
      hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, nextOffset);
      messageText = toMessage(fillingStation);
    } else {
      fillingStation = fillingStationFacade.getBestFuelPrice(latitude, longitude, radius, fuelType, nextOffset);
      hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, nextOffset, fuelType);
      messageText = toMessage(fillingStation, fuelType);
    }

    final InlineKeyboardMarkup replyKeyboard = getInlineKeyboardForAllFillingStations(command, nextOffset, hasNext);

    final EditMessageText editMessageText = editMessageText(chatInfoHolder.getChatId(), messageText,
        callbackHolder.getMessageId(), replyKeyboard);
    return List.of(editMessageText);
  }

  @Override
  public String getCallbackType() {
    return CALLBACK_QUERY;
  }
}
