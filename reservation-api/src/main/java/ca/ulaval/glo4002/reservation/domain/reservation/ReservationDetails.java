package ca.ulaval.glo4002.reservation.domain.reservation;

import java.time.LocalDateTime;

public class ReservationDetails {
  private final Country country;
  private final LocalDateTime reservationDate;

  public ReservationDetails(Country country, LocalDateTime reservationDate) {
    this.country = country;
    this.reservationDate = reservationDate;
  }

  public Country getCountry() {
    return country;
  }

  public LocalDateTime getReservationDate() {
    return reservationDate;
  }
}
