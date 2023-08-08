package md.bot.fuel.domain;

import md.bot.fuel.domain.exception.RFCError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RFCErrorBuilderTest {

    @Test
    @DisplayName("Should return toString for RFCErrorBuilder")
    void shouldReturnToStringForRFCErrorBuilder() {
        final RFCError.RFCErrorBuilder rfcErrorBuilder = RFCError.builder()
                .source("source")
                .reason("reason")
                .message("message")
                .recoverable(true);

        final String builderToString = "RFCError.RFCErrorBuilder(source=source, reason=reason, message=message, " +
                "recoverable=true)";

        assertThat(rfcErrorBuilder.toString()).isEqualTo(builderToString);
    }
}