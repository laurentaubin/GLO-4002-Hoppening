package ca.ulaval.glo4002.reservation.api.reservation;

public class ExceptionResponse {
  private String error;
  private String description;

  public ExceptionResponse(String error, String description) {
    this.error = error;
    this.description = description;
  }

  public String getError() {
    return error;
  }

  public String getDescription() {
    return description;
  }
}
