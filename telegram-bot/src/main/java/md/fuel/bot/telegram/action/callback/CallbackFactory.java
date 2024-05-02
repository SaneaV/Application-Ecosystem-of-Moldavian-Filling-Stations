package md.fuel.bot.telegram.action.callback;

import md.fuel.bot.telegram.action.callback.type.CallbackType;

public interface CallbackFactory {

  CallbackType getCallback(String callbackType);
}
