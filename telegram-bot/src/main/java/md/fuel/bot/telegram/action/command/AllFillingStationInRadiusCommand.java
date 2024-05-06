package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.action.callback.type.CallbackCommand.ALL_FILLING_STATIONS_IN_RADIUS;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.InlineKeyboardMarkupUtil.getInlineKeyboardForAllFillingStations;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.domain.FillingStation;
import md.fuel.bot.facade.FillingStationFacade;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.telegram.dto.UserDataDto;
import md.telegram.lib.action.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllFillingStationInRadiusCommand implements Command {

  public static final String COMMAND = "All filling stations";

  private static final int FIRST_MESSAGE_OFFSET = 0;

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final long chatId = chatInfoHolder.getChatId();
    log.info("Get all filling stations in radius for user = {}", userId);

    final UserDataDto userData = userDataFacade.getUserData(userId);
    final double latitude = userData.getLatitude();
    final double longitude = userData.getLongitude();
    final double radius = userData.getRadius();
    final FillingStation fillingStation = fillingStationFacade.getAllFillingStations(latitude, longitude, radius,
        FIRST_MESSAGE_OFFSET);
    final boolean hasNext = fillingStationFacade.hasNext(latitude, longitude, radius, FIRST_MESSAGE_OFFSET);

    final String messageText = toMessage(fillingStation);
    final SendMessage fillingStationMessage = sendMessage(chatId, messageText);
    final InlineKeyboardMarkup inlineKeyboard = getInlineKeyboardForAllFillingStations(
        ALL_FILLING_STATIONS_IN_RADIUS.getCommandId(), FIRST_MESSAGE_OFFSET, hasNext);
    fillingStationMessage.setReplyMarkup(inlineKeyboard);

    return List.of(fillingStationMessage);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}
