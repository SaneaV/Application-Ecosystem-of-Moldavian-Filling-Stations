package md.fuel.api.rest.exception;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ErrorConstants {

  public static final String ERROR_SOURCE = "MD_FUEL_PRICE_APP";
  public static final String ERROR_REASON_INTERNAL_ERROR = "INTERNAL_SERVER_ERROR";
  public static final String ERROR_REASON_METHOD_ARGUMENT_NOT_VALID = "METHOD_ARGUMENT_NOT_VALID";
  public static final String ERROR_REASON_CONSTRAINT_ERROR = "CONSTRAINT_ERROR";
}
