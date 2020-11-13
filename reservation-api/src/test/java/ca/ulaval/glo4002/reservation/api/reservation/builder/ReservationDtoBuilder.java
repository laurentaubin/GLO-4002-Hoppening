package ca.ulaval.glo4002.reservation.api.reservation.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerApiDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;

public class ReservationDtoBuilder {
  private static final String A_DINNER_DATE = "2150-07-21T15:23:20.142Z";
  private static final double A_RESERVATION_PRICE = 1000;

  private String dinnerDate;
  private double reservationPrice;
  private final List<CustomerApiDto> customers;

  public ReservationDtoBuilder() {
    dinnerDate = A_DINNER_DATE;
    reservationPrice = A_RESERVATION_PRICE;
    customers = new ArrayList<>();
  }

  public ReservationDtoBuilder withDinnerDate(String dinnerDate) {
    this.dinnerDate = dinnerDate;
    return this;
  }

  public ReservationDtoBuilder withReservationPrice(double reservationPrice) {
    this.reservationPrice = reservationPrice;
    return this;
  }

  public ReservationDtoBuilder withCustomer(CustomerApiDto customer) {
    customers.add(customer);
    return this;
  }

  public ReservationDtoBuilder withAnyCustomers() {
    customers.add(new CustomerDtoBuilder().build());
    return this;
  }

  public ReservationDto build() {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setDinnerDate(dinnerDate);
    reservationDto.setCustomers(customers);
    return reservationDto;
  }
}
