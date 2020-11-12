package ca.ulaval.glo4002.reservation.domain;

import java.time.LocalDateTime;

public class ReservationDate {
  private final LocalDateTime reservationDate;

  public ReservationDate(LocalDateTime reservationDate) {
    this.reservationDate = reservationDate;
  }

  public LocalDateTime getLocalDateTime() {
    return reservationDate;
  }
}
