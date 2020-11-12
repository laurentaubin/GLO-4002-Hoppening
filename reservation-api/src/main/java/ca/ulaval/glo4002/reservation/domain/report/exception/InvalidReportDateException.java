package ca.ulaval.glo4002.reservation.domain.report.exception;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidReportDateException extends ReservationException {
  private static final String ERROR = "INVALID_DATE";
  private static final String DESCRIPTION = "Dates should be between %s and %s and must be specified.";
  private static final int STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

  public InvalidReportDateException(String startDate, String endDate) {
    super(ERROR, String.format(DESCRIPTION, startDate, endDate), STATUS_CODE);
  }
}
