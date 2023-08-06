package md.bot.fuel.rest.exception;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ErrorConstants {

    public static final String ERROR_SOURCE = "MD_FUEL_PRICE_APP";
    public static final String ERROR_REASON_INTERNAL_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String ERROR_REASON_BIND_ERROR = "BIND_ERROR";
    public static final String REST_CLIENT = "REST";
}
