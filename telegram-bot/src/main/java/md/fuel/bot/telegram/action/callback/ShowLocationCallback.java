package md.fuel.bot.telegram.action.callback;

import static md.fuel.bot.telegram.action.callback.type.CallbackCommand.ALL_FILLING_STATIONS_IN_RADIUS;
import static md.fuel.bot.telegram.utils.InlineKeyboardMarkupUtil.getInlineKeyboardForLocation;
import static md.fuel.bot.telegram.utils.MessageUtil.deleteMessage;
import static md.fuel.bot.telegram.utils.MessageUtil.sendLocation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.telegram.lib.action.Callback;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowLocationCallback implements Callback {

  private static final String CALLBACK_QUERY = "SHOW_LOCATION";
  private static final String OFFSET = "OFFSET";
  private static final String COMMAND = "COMMAND";
  private static final String FUEL_TYPE = "FUEL_TYPE";

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final CallbackHolder callbackHolder;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(CallbackQuery callbackQuery) {
    final int command = Integer.parseInt(callbackHolder.getCallbackDataBy(COMMAND));
    final int currentOffset = Integer.parseInt(callbackHolder.getCallbackDataBy(OFFSET));
    final long userId = chatInfoHolder.getUserId();

    log.info("Get location of filling station for user = {} and offset = {}", userId, currentOffset);

    final String fuelType = callbackHolder.getCallbackDataBy(FUEL_TYPE);

    final long chatId = chatInfoHolder.getChatId();

    final UserDataDto userData = userDataFacade.getUserData(userId);
    final double latitude = userData.getLatitude();
    final double longitude = userData.getLongitude();
    final double radius = userData.getRadius();

    final FillingStation fillingStation = getFillingStation(command, latitude, longitude, radius, fuelType, currentOffset);

    final InlineKeyboardMarkup inlineKeyboardForLocation = getInlineKeyboardForLocation(command,
        Integer.valueOf(callbackHolder.getCallbackDataBy(OFFSET)), callbackHolder.getCallbackDataBy(FUEL_TYPE));
    final SendLocation fuelStationLocation = sendLocation(chatId, fillingStation.getLatitude(), fillingStation.getLongitude(),
        inlineKeyboardForLocation);
    final DeleteMessage deleteMessage = deleteMessage(chatId, callbackHolder.getMessageId());
    return List.of(fuelStationLocation, deleteMessage);
  }

  private FillingStation getFillingStation(int command, double latitude, double longitude, double radius, String fuelType,
      int currentOffset) {
    if (ALL_FILLING_STATIONS_IN_RADIUS.getCommandId() == command) {
      return fillingStationFacade.getAllFillingStations(latitude, longitude, radius, currentOffset);
    }
    return fillingStationFacade.getBestFuelPrice(latitude, longitude, radius, fuelType, currentOffset);
  }

  @Override
  public String getCallbackType() {
    return CALLBACK_QUERY;
  }
}