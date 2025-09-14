package md.fuel.bot.telegram.action.command;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.converter.MessageConverter.toMessage;
import static md.fuel.bot.telegram.utils.MessageUtil.sendLocation;
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
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class NearestFillingStationCommand implements Command {

  public static final String COMMAND = "nearest-filling-station.message";

  private final FillingStationFacade fillingStationFacade;
  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    log.info("Get nearest filling station for user = {}", userId);

    final long chatId = chatInfoHolder.getChatId();
    final UserDataDto userData = userDataFacade.getUserData(userId);
    final String language = userData.getLanguage();
    final FillingStation nearestFuelStation = fillingStationFacade.getNearestFillingStation(userData.getLatitude(),
        userData.getLongitude(), userData.getRadius());

    final String fuelStationTextMessage = toMessage(nearestFuelStation, translatorService, language);
    final ReplyKeyboardMarkup mainMenuKeyboard = getMainMenuKeyboard(translatorService, language);
    final SendMessage fuelStationMessage = sendMessage(chatId, fuelStationTextMessage, mainMenuKeyboard);
    final SendLocation fuelStationLocation = sendLocation(chatId, nearestFuelStation.getLatitude(),
        nearestFuelStation.getLongitude());

    return asList(fuelStationMessage, fuelStationLocation);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}