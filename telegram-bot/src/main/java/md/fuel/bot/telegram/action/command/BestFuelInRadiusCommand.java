package md.fuel.bot.telegram.action.command;

import static md.fuel.bot.telegram.action.callback.type.CallbackCommand.BEST_FUEL_IN_RADIUS;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.InlineKeyboardMarkupUtil.getInlineKeyboardForBestFuelPriceStation;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.dto.UserDataDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
public class BestFuelInRadiusCommand implements Command {

  private static final String LAST_MESSAGE = "Use the ⬅️ and ➡️ buttons to view available filling stations.";
  private static final int FIRST_MESSAGE_OFFSET = 0;

  public static List<String> COMMAND;

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;

  public BestFuelInRadiusCommand(FillingStationFacade fillingStationFacade, UserDataFacade userDataFacade,
      ChatInfoHolder chatInfoHolder) {
    this.fillingStationFacade = fillingStationFacade;
    this.userDataFacade = userDataFacade;
    this.chatInfoHolder = chatInfoHolder;
    COMMAND = fillingStationFacade.getSupportedFuelTypes().getFuelTypes();
  }

  @Override
  public List<? super PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final long chatId = chatInfoHolder.getChatId();
    log.info("Get all filling stations in radius with best fuel price for user = {}", userId);

    final String fuelType = update.getMessage().getText();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final double latitude = userData.getLatitude();
    final double longitude = userData.getLongitude();
    final double radius = userData.getRadius();
    final FillingStation fillingStation = fillingStationFacade.getBestFuelPrice(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius(), fuelType, FIRST_MESSAGE_OFFSET);
    final boolean hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, FIRST_MESSAGE_OFFSET, fuelType);

    final String messageText = toMessage(fillingStation, fuelType);
    final SendMessage fillingStationMessage = sendMessage(chatId, messageText);
    final InlineKeyboardMarkup inlineKeyboard = getInlineKeyboardForBestFuelPriceStation(BEST_FUEL_IN_RADIUS.getCommandId(),
        FIRST_MESSAGE_OFFSET, fuelType, hasNext);
    fillingStationMessage.setReplyMarkup(inlineKeyboard);
    final SendMessage backToMenu = sendMessage(chatId, LAST_MESSAGE, getMainMenuKeyboard());

    return List.of(fillingStationMessage, backToMenu);
  }

  @Override
  public List<String> getCommands() {
    return COMMAND;
  }
}