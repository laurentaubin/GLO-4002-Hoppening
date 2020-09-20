package ca.ulaval.glo4002.reservation.service.validator;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.domain.RestrictionType;
import java.util.List;

public class RestrictionValidator {

  public RestrictionValidator() {
  }

  public void validate(List<CustomerDto> customers) {
    customers.stream()
             .flatMap(customer -> customer.getRestrictions().stream())
             .forEachOrdered(RestrictionType::valueOfName);
  }
}
