package md.fuel.bot.telegram.action.command;

import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getStationTypeMenuKeyboard;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.telegram.lib.action.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeStationTypeCommand implements Command {

  private static final String COMMAND = "change-station-type.message";
  private static final String MESSAGE = "select-station-type.message";

  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);
    log.info("Show change station type menu for user = {}", userId);

    final String messageText = translatorService.translate(language, MESSAGE);
    final ReplyKeyboardMarkup stationTypeMenuKeyboard = getStationTypeMenuKeyboard(translatorService, language);

    final SendMessage message = sendMessage(chatInfoHolder.getChatId(), messageText, stationTypeMenuKeyboard);
    return singletonList(message);
  }

  @Override
  public List<String> getCommands() {
    return singletonList(COMMAND);
  }
}