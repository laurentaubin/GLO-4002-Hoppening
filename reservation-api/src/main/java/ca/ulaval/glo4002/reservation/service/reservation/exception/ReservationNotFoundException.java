package ca.ulaval.glo4002.reservation.service.reservation.exception;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class ReservationNotFoundException extends ReservationException {
  public static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.RESERVATION_NOT_FOUND;

  public ReservationNotFoundException(ReservationId reservationId) {
    super(ERROR_CODE.toString(),
          ERROR_CODE.getMessage()
                    .replace("RESERVATION_NUMBER", String.valueOf(reservationId.getLongId())),
          ERROR_CODE.getCode());
  }
}
