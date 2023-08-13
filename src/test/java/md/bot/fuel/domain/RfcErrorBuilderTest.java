package md.bot.fuel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import md.bot.fuel.domain.exception.RfcError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RfcErrorBuilderTest {

  @Test
  @DisplayName("Should return toString for RFCErrorBuilder")
  void shouldReturnToStringForRfcErrorBuilder() {
    final RfcError.RfcErrorBuilder rfcErrorBuilder = RfcError.builder()
        .source("source")
        .reason("reason")
        .message("message")
        .recoverable(true);

    final String builderToString = "RfcError.RfcErrorBuilder(source=source, reason=reason, message=message, recoverable=true)";

    assertThat(rfcErrorBuilder.toString()).isEqualTo(builderToString);
  }
}