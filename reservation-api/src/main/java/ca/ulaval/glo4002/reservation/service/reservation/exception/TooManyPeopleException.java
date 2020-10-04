package ca.ulaval.glo4002.reservation.service.reservation.exception;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class TooManyPeopleException extends ReservationException {
  public static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.TOO_MANY_PEOPLE;

  public TooManyPeopleException() {
    super(ERROR_CODE.toString(), ERROR_CODE.getMessage(), ERROR_CODE.getCode());
  }
}
