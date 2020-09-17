package ca.ulaval.glo4002.reservation.service.exception;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidReservationDateException extends ReservationException {
  public static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.INVALID_RESERVATION_DATE;

  public InvalidReservationDateException() {
    super(ERROR_CODE.toString(), ERROR_CODE.getMessage(), ERROR_CODE.getCode());
  }
}
