package md.fuel.bot.infrastructure.configuration;

import lombok.extern.slf4j.Slf4j;
import md.fuel.bot.infrastructure.exception.GatewayErrorDescription;
import md.fuel.bot.infrastructure.exception.model.GatewayPassThroughException;
import md.fuel.bot.infrastructure.repository.GatewayError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class WebClientErrorHandlingFilterFunctionConfig {

  @Bean
  public ExchangeFilterFunction handleServerErrorFilterFunction() {
    log.info("Initialize server error filter function handler");
    return ExchangeFilterFunction.ofResponseProcessor(response -> {
      if (response.statusCode().isError()) {
        log.info("Wrap exception received from the API request");
        return response.bodyToMono(new ParameterizedTypeReference<GatewayErrorDescription<GatewayError>>() {
        }).flatMap(error -> {
          final HttpStatus httpStatus = HttpStatus.valueOf(response.statusCode().value());
          return handleGatewayExceptionResponse(error, httpStatus);
        });
      }
      return Mono.just(response);
    });
  }

  private Mono<ClientResponse> handleGatewayExceptionResponse(GatewayErrorDescription<GatewayError> error,
      HttpStatus httpStatus) {
    return Mono.error(new GatewayPassThroughException(error, httpStatus));
  }
}
