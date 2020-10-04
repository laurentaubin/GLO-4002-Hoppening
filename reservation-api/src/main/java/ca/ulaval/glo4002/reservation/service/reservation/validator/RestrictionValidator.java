package ca.ulaval.glo4002.reservation.service.reservation.validator;

import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public class RestrictionValidator {

  public RestrictionValidator() {
  }

  public void validate(List<CustomerDto> customers) {
    customers.stream()
             .flatMap(customer -> customer.getRestrictions().stream())
             .forEachOrdered(RestrictionType::valueOfName);
  }
}
