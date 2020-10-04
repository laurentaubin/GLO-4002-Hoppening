package ca.ulaval.glo4002.reservation.domain.report.exception;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidReportTypeException extends ReservationException {
  private static final String ERROR = "INVALID_TYPE";
  private static final String DESCRIPTION = "Type must be either total or unit and must be specified.";
  private static final int STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

  public InvalidReportTypeException() {
    super(ERROR, DESCRIPTION, STATUS_CODE);
  }
}
