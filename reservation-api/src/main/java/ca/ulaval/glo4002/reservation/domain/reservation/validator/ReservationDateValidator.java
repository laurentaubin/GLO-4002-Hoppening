package ca.ulaval.glo4002.reservation.domain.reservation.validator;

import java.time.LocalDateTime;

import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationDateException;

public class ReservationDateValidator extends DateValidator {
  public ReservationDateValidator(String dateFormat, String openingDate, String closingDate) {
    super(dateFormat, openingDate, closingDate);
  }

  public void validate(String date) {
    LocalDateTime parsedDate = LocalDateTime.parse(date, this.getDateTimeFormatter());
    if (parsedDate.isBefore(this.getOpeningDate()) || parsedDate.isAfter(this.getClosingDate())) {
      throw new InvalidReservationDateException();
    }
  }
}
