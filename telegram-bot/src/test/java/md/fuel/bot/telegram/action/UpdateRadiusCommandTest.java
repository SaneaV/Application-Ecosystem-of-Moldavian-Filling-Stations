//package md.fuel.bot.telegram.action;
//
//import static java.util.Collections.emptyList;
//import static md.fuel.bot.telegram.utils.ReplyKeyboardMarkupUtil.getMainMenuKeyboard;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
//import java.util.List;
//import md.fuel.bot.facade.UserDataFacade;
//import md.fuel.bot.infrastructure.configuration.ChatInfoHolder;
//import md.fuel.bot.telegram.action.command.BestFuelInRadiusCommand;
//import md.fuel.bot.telegram.action.command.UpdateRadiusCommand;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//public class UpdateRadiusCommandTest {
//
//  private static final String MESSAGE = "New radius set!";
//  private static final long CHAT_ID = 20L;
//  private static final long USER_ID = 10L;
//
//  private final UpdateRadiusCommand updateRadiusCommand;
//  private final UserDataFacade userDataFacade;
//
//  public UpdateRadiusCommandTest() {
//    this.userDataFacade = mock(UserDataFacade.class);
//    final ChatInfoHolder chatInfoHolder = new ChatInfoHolder();
//    chatInfoHolder.setChatInfo(USER_ID, CHAT_ID);
//
//    BestFuelInRadiusCommand.COMMAND = emptyList();
//
//    this.updateRadiusCommand = new UpdateRadiusCommand(userDataFacade, chatInfoHolder);
//  }
//
//  @Test
//  @DisplayName("Should return radius updated message")
//  void shouldReturnRadiusUpdatedMessage() {
//    final double radius = 10.0;
//
//    final Update update = new Update();
//    final Message message = new Message();
//
//    message.setText(Double.toString(radius));
//    update.setMessage(message);
//
//    final List<SendMessage> messages = updateRadiusCommand.execute(update).stream()
//        .map(m -> (SendMessage) m)
//        .toList();
//
//    assertThat(messages).hasSize(1);
//    final SendMessage sendMessage = messages.get(0);
//    assertThat(sendMessage.getText()).isEqualTo(MESSAGE);
//    assertThat(sendMessage.getChatId()).isEqualTo(Long.toString(CHAT_ID));
//    assertThat(sendMessage.getReplyMarkup()).isEqualTo(getMainMenuKeyboard());
//
//    verify(userDataFacade).updateRadius(USER_ID, radius);
//  }
//
//  @Test
//  @DisplayName("Should return command")
//  void shouldReturnCommand() {
//    final List<String> commands = updateRadiusCommand.getCommands();
//    assertThat(commands).isEmpty();
//  }
//}
