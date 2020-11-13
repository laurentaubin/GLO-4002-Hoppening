package ca.ulaval.glo4002.reservation.domain.reservation;

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
    return customerRestrictionsNames.stream().map(RestrictionType::valueOfName)
        .collect(Collectors.toSet());
  }
}
