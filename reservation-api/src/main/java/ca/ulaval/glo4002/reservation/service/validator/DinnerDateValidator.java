package ca.ulaval.glo4002.reservation.service.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import ca.ulaval.glo4002.reservation.api.reservation.exception.InvalidFormatException;
import ca.ulaval.glo4002.reservation.service.exception.InvalidDinnerDateException;

public class DinnerDateValidator {
  private DateTimeFormatter dateTimeFormatter;
  private LocalDateTime openingDate;
  private LocalDateTime closingDate;

  public DinnerDateValidator(String dateFormat, String openingDate, String closingDate) {
    dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    this.openingDate = LocalDateTime.parse(openingDate, dateTimeFormatter);
    this.closingDate = LocalDateTime.parse(closingDate, dateTimeFormatter);
  }

  public void validateDate(String date) {
    try {
      LocalDateTime parsedDate = LocalDateTime.parse(date, dateTimeFormatter);
      validateDateRange(parsedDate);
    } catch (DateTimeParseException e) {
      throw new InvalidFormatException();
    }
  }

  private void validateDateRange(LocalDateTime date) {
    if (date.isBefore(openingDate) || date.isAfter(closingDate)) {
      throw new InvalidDinnerDateException();
    }
  }
}
