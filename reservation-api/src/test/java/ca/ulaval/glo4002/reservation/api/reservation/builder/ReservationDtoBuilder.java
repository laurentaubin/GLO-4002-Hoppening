package ca.ulaval.glo4002.reservation.api.reservation.builder;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CreateReservationRequestDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.ReservationDto;

import java.util.ArrayList;
import java.util.List;

public class ReservationDtoBuilder {
    private static final String A_DINNER_DATE = "2150-07-21T15:23:20.142Z";

    private String dinnerDate;
    private final List<CustomerDto> customers;

    public ReservationDtoBuilder() {
        dinnerDate = A_DINNER_DATE;
        customers = new ArrayList<>();
    }

    public ReservationDtoBuilder withDinnerDate(String dinnerDate) {
        this.dinnerDate = dinnerDate;
        return this;
    }

    public ReservationDtoBuilder withCustomer(CustomerDto customer) {
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
