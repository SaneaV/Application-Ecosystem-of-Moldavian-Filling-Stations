package md.bot.fuel.rest.exception;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import md.bot.fuel.domain.exception.Errors;
import md.bot.fuel.infrastructure.exception.ErrorDescriptionResponse;

@Getter
public class GatewayErrorDescription implements ErrorDescriptionResponse {

    @Getter(onMethod_ = {@JsonGetter("Errors")})
    private final Errors errors = new Errors();
}
