package ca.ulaval.glo4002.reservation.api.report.exception;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidReportDateException extends ReservationException {
  private static final String ERROR = "INVALID_DATE";
  private static final String DESCRIPTION = "Dates should be between July 20 2150 and July 30 2150 and must be specified.";
  private static final int STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

  public InvalidReportDateException() {
    super(ERROR, DESCRIPTION, STATUS_CODE);
  }
}
