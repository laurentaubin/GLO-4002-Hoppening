package ca.ulaval.glo4002.reservation.service.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateValidator {
  private DateTimeFormatter dateTimeFormatter;
  private LocalDateTime openingDate;
  private LocalDateTime closingDate;

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
