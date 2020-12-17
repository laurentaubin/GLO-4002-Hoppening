package ca.ulaval.glo4002.reservation.service.reservation;

import ca.ulaval.glo4002.reservation.domain.ReservationRequest;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDate;
import ca.ulaval.glo4002.reservation.domain.date.DinnerDateFactory;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDate;
import ca.ulaval.glo4002.reservation.domain.date.ReservationDateFactory;
import ca.ulaval.glo4002.reservation.domain.hoppening.HoppeningEvent;
import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Reservation;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationId;
import ca.ulaval.glo4002.reservation.domain.reservation.ReservationIdFactory;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.service.reservation.dto.CustomerDto;

public class CustomerFactory {
  public Customer create(CustomerDto customerDto) {
    return new Customer(customerDto.getName(),
                        createCustomerRestrictions(customerDto.getRestrictions()));
  }

  private Set<RestrictionType> createCustomerRestrictions(List<String> customerRestrictionsNames) {
    return customerRestrictionsNames.stream()
                                    .map(RestrictionType::valueOfName)
                                    .collect(Collectors.toSet());
  }

}
