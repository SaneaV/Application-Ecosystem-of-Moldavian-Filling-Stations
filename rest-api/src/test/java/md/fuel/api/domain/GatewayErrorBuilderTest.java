package md.fuel.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import md.fuel.api.domain.exception.GatewayError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GatewayErrorBuilderTest {

  @Test
  @DisplayName("Should return toString for GatewayErrorBuilder")
  void shouldReturnToStringForGatewayErrorBuilder() {
    final GatewayError.GatewayErrorBuilder gatewayErrorBuilder = GatewayError.builder()
        .source("source")
        .reasonCode("reasonCode")
        .description("description")
        .recoverable(true);

    final String builderToString = "GatewayError.GatewayErrorBuilder(source=source, reasonCode=reasonCode, "
        + "description=description, recoverable=true)";

    assertThat(gatewayErrorBuilder.toString()).isEqualTo(builderToString);
  }
}