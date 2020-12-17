package ca.ulaval.glo4002.reservation.api.reservation.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ReservationDetailsDto {
  @Valid
  @NotNull
  private CountryDto country;

  @NotNull
  private String reservationDate;

  public void setCountry(CountryDto countryDto) {
    this.country = countryDto;
  }

  public String getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(String reservationDate) {
    this.reservationDate = reservationDate;
  }
}
