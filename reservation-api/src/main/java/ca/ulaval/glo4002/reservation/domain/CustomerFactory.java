package ca.ulaval.glo4002.reservation.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;
import ca.ulaval.glo4002.reservation.service.reservation.CustomerObject;

public class CustomerFactory {
  public Customer create(CustomerObject customerObject) {
    return new Customer(customerObject.getName(),
                        createCustomerRestrictions(customerObject.getRestrictions()));
  }

  private Set<RestrictionType> createCustomerRestrictions(List<String> customerRestrictionsNames) {
    return customerRestrictionsNames.stream()
                                    .map(RestrictionType::valueOfName)
                                    .collect(Collectors.toSet());
  }
}
