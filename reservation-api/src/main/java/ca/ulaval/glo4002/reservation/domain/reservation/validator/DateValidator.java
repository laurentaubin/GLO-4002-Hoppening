package ca.ulaval.glo4002.reservation.domain.reservation.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateValidator {
  private final DateTimeFormatter dateTimeFormatter;
  private final LocalDateTime openingDate;
  private final LocalDateTime closingDate;

  public DateValidator(String dateFormat, String openingDate, String closingDate) {
    dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    this.openingDate = LocalDateTime.parse(openingDate, dateTimeFormatter);
    this.closingDate = LocalDateTime.parse(closingDate, dateTimeFormatter);
  }

  public DateTimeFormatter getDateTimeFormatter() {
    return dateTimeFormatter;
  }

  public LocalDateTime getOpeningDate() {
    return openingDate;
  }

  public LocalDateTime getClosingDate() {
    return closingDate;
  }
}
