package md.fuel.bot.telegram.validation;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserStatusValidator {

  boolean checkIfUserBlockedBot(Update update);
}