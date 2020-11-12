package ca.ulaval.glo4002.reservation.service.reservation.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.api.reservation.ReservationErrorCode;
import ca.ulaval.glo4002.reservation.exception.ReservationException;

public class InvalidReservationDateException extends ReservationException {
  private static final String PATTERN_FORMAT = "MMMM dd YYYY";
  public static final ReservationErrorCode ERROR_CODE = ReservationErrorCode.INVALID_RESERVATION_DATE;

  public InvalidReservationDateException(LocalDate startDate, LocalDate endDate) {
    super(ERROR_CODE.toString(),
          String.format(ERROR_CODE.getMessage(),
                        startDate.format(DateTimeFormatter.ofPattern(PATTERN_FORMAT)),
                        endDate.format(DateTimeFormatter.ofPattern(PATTERN_FORMAT))),
          ERROR_CODE.getCode());
  }
}
