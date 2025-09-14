package md.fuel.bot.telegram.exception;

import static java.util.Objects.isNull;
import static md.fuel.bot.telegram.exception.ErrorCode.ERROR_CODE_SPECIFY_LANGUAGE;
import static md.fuel.bot.telegram.utils.MessageUtil.sendMessage;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getLanguageMenuKeyboard;
import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;

import java.util.HashMap;
import java.util.Map;
import md.fuel.bot.facade.UserDataFacade;
import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
import md.fuel.bot.infrastructure.exception.model.ClientRequestException;
import md.fuel.bot.infrastructure.exception.model.EntityNotFoundException;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.exception.model.InfrastructureException;
import md.fuel.bot.infrastructure.service.TranslatorService;
import md.fuel.bot.telegram.converter.ThirdPartyTranslationConverter;
import md.telegram.lib.exception.TelegramException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class TelegramWrappingStrategyImpl implements TelegramWrappingStrategy {

  private static final String UNKNOWN_ERROR = "error.unknown-error.message";

  private final Map<String, ReplyKeyboardMarkup> ERROR_CODE_KEYBOARD;
  private final ChatInfoHolder chatInfoHolder;
  private final UserDataFacade userDataFacade;
  private final TranslatorService translatorService;

  public TelegramWrappingStrategyImpl(ChatInfoHolder chatInfoHolder, UserDataFacade userDataFacade,
      TranslatorService translatorService) {
    this.chatInfoHolder = chatInfoHolder;
    this.translatorService = translatorService;
    this.userDataFacade = userDataFacade;

    ERROR_CODE_KEYBOARD = new HashMap<>();
    final ReplyKeyboardMarkup languageMenuKeyboard = getLanguageMenuKeyboard(translatorService);
    ERROR_CODE_KEYBOARD.put(ERROR_CODE_SPECIFY_LANGUAGE, languageMenuKeyboard);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleRuntimeException(RuntimeException exception) {
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);
    final String translatedMessage = translatorService.translate(language, UNKNOWN_ERROR);

    return prepareErrorMessage(translatedMessage);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleTelegramException(TelegramException exception) {
    final String keyForTranslation = ThirdPartyTranslationConverter.getKeyForThirdPartyMessage(exception.getMessage());
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);
    final String translatedMessage = translatorService.translate(language, keyForTranslation);

    return prepareErrorMessage(translatedMessage);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleEntityNotFoundException(EntityNotFoundException exception) {
    if (isNull(exception.getExceptionCode())) {
      return prepareErrorMessage(exception.getMessage());
    } else {
      final ReplyKeyboardMarkup replyKeyboardMarkup = ERROR_CODE_KEYBOARD.get(exception.getExceptionCode());
      return prepareErrorMessageWithKeyboard(exception.getMessage(), replyKeyboardMarkup);
    }
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleInfrastructureException(InfrastructureException exception) {
    return prepareErrorMessage(exception.getMessage());
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleGatewayPassThroughException(GatewayPassThroughException exception) {
    final String exceptionMessage = exception.getGatewayError().getErrors().getError().get(0).getDescription();
    final String keyForTranslation = ThirdPartyTranslationConverter.getKeyForThirdPartyMessage(exceptionMessage);
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);
    final String translatedMessage = translatorService.translate(language, keyForTranslation);

    return prepareErrorMessage(translatedMessage);
  }

  @Override
  public ResponseEntity<BotApiMethod<?>> handleClientRequestException(ClientRequestException exception) {
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);
    final String translatedMessage = translatorService.translate(language, exception.getMessage());

    return prepareErrorMessage(translatedMessage);
  }

  private ResponseEntity<BotApiMethod<?>> prepareErrorMessage(String errorText) {
    final long userId = chatInfoHolder.getUserId();
    final String language = userDataFacade.getLanguage(userId);

    final ReplyKeyboardMarkup mainMenuKeyboard = getMainMenuKeyboard(translatorService, language);
    final SendMessage errorMessage = sendMessage(chatInfoHolder.getChatId(), errorText, mainMenuKeyboard);

    return ResponseEntity.ok().body(errorMessage);
  }

  private ResponseEntity<BotApiMethod<?>> prepareErrorMessageWithKeyboard(String errorText, ReplyKeyboardMarkup keyboardMarkup) {
    final SendMessage errorMessage = sendMessage(chatInfoHolder.getChatId(), errorText, keyboardMarkup);
    return ResponseEntity.ok().body(errorMessage);
  }
}