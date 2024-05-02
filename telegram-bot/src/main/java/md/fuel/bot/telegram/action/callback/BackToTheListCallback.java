package md.fuel.bot.telegram.action.callback;

import static md.fuel.bot.telegram.action.callback.type.CallbackCommand.ALL_FILLING_STATIONS_IN_RADIUS;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.InlineKeyboardMarkupUtil.getInlineKeyboardForAllFillingStations;
import static md.fuel.bot.telegram.utils.MessageUtil.deleteMessage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.fuel.bot.telegram.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackToTheListCallback implements Callback {

  private static final String CALLBACK_QUERY = "BACK_TO_MENU";
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
    final int lastOffset = Integer.parseInt(callbackHolder.getCallbackDataBy(OFFSET));
    log.info("Go back to the filling station list for user = {} and offset = {}", userId, lastOffset);

    final long chatId = chatInfoHolder.getChatId();

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
      fillingStation = fillingStationFacade.getAllFillingStations(latitude, longitude, radius, lastOffset);
      hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, lastOffset);
      messageText = toMessage(fillingStation);
    } else {
      fillingStation = fillingStationFacade.getBestFuelPrice(latitude, longitude, radius, fuelType, lastOffset);
      hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, lastOffset, fuelType);
      messageText = toMessage(fillingStation, fuelType);
    }

    final InlineKeyboardMarkup replyKeyboard = getInlineKeyboardForAllFillingStations(command, lastOffset, hasNext);

    final SendMessage sendMessage = MessageUtil.sendMessage(chatId, messageText, replyKeyboard);
    final DeleteMessage deleteMessage = deleteMessage(chatId, callbackHolder.getMessageId());
    return List.of(sendMessage, deleteMessage);
  }

  @Override
  public String getCallbackType() {
    return CALLBACK_QUERY;
  }
}
