package ca.ulaval.glo4002.reservation.exception;

public abstract class ConfigurationException extends RuntimeException {
  private final String error;
  private final String description;
  private final int statusCode;

  public ConfigurationException(String error, String description, int statusCode) {
    this.error = error;
    this.description = description;
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getError() {
    return error;
  }

  public String getDescription() {
    return description;
  }
}
