package md.fuel.bot.infrastructure.service;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslatorService {

  private final MessageSource messageSource;

  public String translate(String lang, String key) {
    log.info("Translate key: {}, to lang: {}", key, lang);
    final Locale locale = Locale.forLanguageTag(lang);
    return messageSource.getMessage(key, null, locale);
  }
}