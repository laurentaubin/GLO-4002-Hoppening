package ca.ulaval.glo4002.reservation.api.reservation.exception;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidFormatException extends ReservationException {
  private static final String ERROR = "INVALID_FORMAT";
  private static final String DESCRIPTION = "Invalid Format";
  private static final int STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

  public InvalidFormatException() {
    super(ERROR, DESCRIPTION, STATUS_CODE);
  }
}
