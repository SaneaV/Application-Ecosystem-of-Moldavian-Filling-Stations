package md.bot.fuel.rest.exception;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RfcErrorDescriptionBuilderTest {

  @Test
  @DisplayName("Should return toString for RFCErrorDescriptionBuilder")
  void shouldReturnToStringForRfcErrorDescriptionBuilder() {
    final RfcErrorDescription.RfcErrorDescriptionBuilder rfcErrorDescriptionBuilder = RfcErrorDescription.builder()
        .type("type")
        .correlationId("correlationId")
        .status(1)
        .detail("detail")
        .title("title")
        .errorDetails(emptyList());

    final String builderToString = "RfcErrorDescription.RfcErrorDescriptionBuilder(status=1, detail=detail, title=title, "
        + "type=type, correlationId=correlationId, errorDetails=[])";

    assertThat(rfcErrorDescriptionBuilder.toString()).isEqualTo(builderToString);
  }
}