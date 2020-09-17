package ca.ulaval.glo4002.reservation.domain.builder;

import java.time.LocalDateTime;

import ca.ulaval.glo4002.reservation.domain.Country;
import ca.ulaval.glo4002.reservation.domain.ReservationDetails;

public class ReservationDetailsBuilder {
  private static final LocalDateTime A_RESERVATION_DATE = LocalDateTime.now();

  private Country country;
  private LocalDateTime reservationDate;

  public ReservationDetailsBuilder() {
    this.country = new CountryBuilder().build();
    this.reservationDate = A_RESERVATION_DATE;
  }

  public ReservationDetailsBuilder withCountry(Country country) {
    this.country = country;
    return this;
  }

  public ReservationDetailsBuilder withReservationDate(LocalDateTime reservationDate) {
    this.reservationDate = reservationDate;
    return this;
  }

  public ReservationDetails build() {
    return new ReservationDetails(this.country, this.reservationDate);
  }
}
