package ca.ulaval.glo4002.reservation.service.reservation.validator;

import java.time.LocalDateTime;

import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidDinnerDateException;

public class DinnerDateValidator extends DateValidator {

  public DinnerDateValidator(String dateFormat, String openingDate, String closingDate) {
    super(dateFormat, openingDate, closingDate);
  }

  public void validate(String date) {
    LocalDateTime parsedDate = LocalDateTime.parse(date, this.getDateTimeFormatter());
    if (parsedDate.isBefore(this.getOpeningDate()) || parsedDate.isAfter(this.getClosingDate())) {
      throw new InvalidDinnerDateException();
    }
  }
}
