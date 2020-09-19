package ca.ulaval.glo4002.reservation.api.reservation.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import ca.ulaval.glo4002.reservation.api.reservation.exception.InvalidFormatException;

public class DateFormatValidator {

  private final DateTimeFormatter dateTimeFormatter;

  public DateFormatValidator(String dateFormat) {
    dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
  }

  public void validateFormat(String date) {
    try {
      LocalDateTime.parse(date, dateTimeFormatter);
    } catch (DateTimeParseException e) {
      throw new InvalidFormatException();
    }
  }
}
