package ca.ulaval.glo4002.reservation.api.configuration.exception;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidDateException extends ReservationException {
  private static final String ERROR = "INVALID_DATE";
  private static final String DESCRIPTION = "Invalid dates, please use the format yyyy-mm-dd";
  private static final int STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

  public InvalidDateException() {
    super(ERROR, DESCRIPTION, STATUS_CODE);
  }
}
