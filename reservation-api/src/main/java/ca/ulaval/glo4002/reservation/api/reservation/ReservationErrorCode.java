package ca.ulaval.glo4002.reservation.api.reservation;

import javax.ws.rs.core.Response;

public enum ReservationErrorCode {
  INVALID_FORMAT("Invalid Format", Response.Status.BAD_REQUEST.getStatusCode()),
  INVALID_DINNER_DATE("Dinner date should be between July 20 2150 and July 30 2150",
                      Response.Status.BAD_REQUEST.getStatusCode()),
  INVALID_RESERVATION_DATE("Reservation date should be between January 1 2150 and July 16 2150",
                           Response.Status.BAD_REQUEST.getStatusCode()),
  INVALID_RESERVATION_QUANTITY("Reservations must include tables and customers",
                               Response.Status.BAD_REQUEST.getStatusCode()),
  TOO_MANY_PEOPLE("The reservation tries to bring a number of people which does not comply with recent governement laws.",
                  Response.Status.BAD_REQUEST.getStatusCode()),
  RESERVATION_NOT_FOUND("Reservation with number RESERVATION_NUMBER not found",
                        Response.Status.NOT_FOUND.getStatusCode());

  private final String message;
  private final int code;

  ReservationErrorCode(String message, int code) {
    this.message = message;
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public int getCode() {
    return code;
  }
}
