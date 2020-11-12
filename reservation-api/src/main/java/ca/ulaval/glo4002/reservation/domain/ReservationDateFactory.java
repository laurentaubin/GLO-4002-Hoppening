package ca.ulaval.glo4002.reservation.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.ulaval.glo4002.reservation.service.reservation.exception.InvalidReservationDateException;

public class ReservationDateFactory {
  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

  public ReservationDate create(String reservationDateStringify, Period period) {
    validate(reservationDateStringify, period);
    return new ReservationDate(LocalDateTime.parse(reservationDateStringify, dateTimeFormatter));
  }

  private void validate(String date, Period period) {
    LocalDateTime parsedDate = LocalDateTime.parse(date, dateTimeFormatter);
    if (!period.isWithinPeriod(parsedDate.toLocalDate())) {
      throw new InvalidReservationDateException(period.getStartDate(), period.getEndDate());
    }
  }
}
