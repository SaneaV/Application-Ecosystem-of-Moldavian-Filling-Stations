package md.fuel.bot.telegram.action.command;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

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
public class UpdateRadiusCommand implements Command {

  private static final String MESSAGE = "radius-set.message";

  private final UserDataFacade userDataFacade;
  private final ChatInfoHolder chatInfoHolder;
  private final TranslatorService translatorService;

  @Override
  public List<? extends PartialBotApiMethod<?>> execute(Update update) {
    final long userId = chatInfoHolder.getUserId();
    log.info("Update radius for user with id = {}", userId);

    final String newRadius = update.getMessage().getText();

    userDataFacade.updateRadius(userId, Double.parseDouble(newRadius));

    final String language = userDataFacade.getUserData(userId).getLanguage();
    final String message = translatorService.translate(language, MESSAGE);

    final ReplyKeyboardMarkup mainMenuKeyboard = getMainMenuKeyboard(translatorService, language);
    final SendMessage sendMessage = sendMessage(chatInfoHolder.getChatId(), message, mainMenuKeyboard);
    return singletonList(sendMessage);
  }

  @Override
  public List<String> getCommands() {
    return emptyList();
  }
}
