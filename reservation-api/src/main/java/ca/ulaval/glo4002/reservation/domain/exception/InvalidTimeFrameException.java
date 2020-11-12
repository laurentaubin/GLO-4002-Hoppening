package ca.ulaval.glo4002.reservation.domain.exception;

import javax.ws.rs.core.Response;

import ca.ulaval.glo4002.reservation.exception.ConfigurationException;

public class InvalidTimeFrameException extends ConfigurationException {
  private static final String ERROR_CODE = "INVALID_TIME_FRAMES";
  private static final String ERROR_DESCRIPTION = "Invalid time frames, please use better ones.";
  private static final Integer ERROR_STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

  public InvalidTimeFrameException() {
    super(ERROR_CODE, ERROR_DESCRIPTION, ERROR_STATUS_CODE);
  }
}