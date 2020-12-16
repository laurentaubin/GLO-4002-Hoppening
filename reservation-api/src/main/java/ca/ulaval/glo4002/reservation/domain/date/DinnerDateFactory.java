package ca.ulaval.glo4002.reservation.domain.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidDinnerDateException;

public class DinnerDateFactory {
  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  public DinnerDate create(String dinnerDateStringify, Period period) {
    validate(dinnerDateStringify, period);
    return new DinnerDate(LocalDateTime.parse(dinnerDateStringify, dateTimeFormatter));
  }

  private void validate(String date, Period period) {
    LocalDateTime parsedDate = LocalDateTime.parse(date, dateTimeFormatter);
    if (!period.isWithinPeriod(parsedDate.toLocalDate())) {
      throw new InvalidDinnerDateException(period.getStartDate(), period.getEndDate());
    }
  }
}
