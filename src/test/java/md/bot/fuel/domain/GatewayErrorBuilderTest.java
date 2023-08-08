package md.bot.fuel.domain;

import md.bot.fuel.domain.exception.GatewayError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GatewayErrorBuilderTest {

    @Test
    @DisplayName("Should return toString for GatewayErrorBuilder")
    void shouldReturnToStringForRFCErrorBuilder() {
        final GatewayError.GatewayErrorBuilder gatewayErrorBuilder = GatewayError.builder()
                .source("source")
                .reasonCode("reasonCode")
                .description("description")
                .recoverable(true);

        final String builderToString = "GatewayError.GatewayErrorBuilder(source=source, reasonCode=reasonCode, " +
                "description=description, recoverable=true)";

        assertThat(gatewayErrorBuilder.toString()).isEqualTo(builderToString);
    }
}