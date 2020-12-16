package ca.ulaval.glo4002.reservation.exception;

public class InvalidRestrictionException extends RuntimeException {
  private static final String error = "INVALID_FORMAT";
  private static final String description = "Invalid Format";

  public String getError() {
    return error;
  }

  public String getDescription() {
    return description;
  }
}
