package ca.ulaval.glo4002.reservation.service.assembler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.RestrictionType;

public class CustomerAssembler {
  public Customer assembleFromCustomerDto(CustomerDto customerDto) {
    Set<RestrictionType> restrictions = customerDto.getRestrictions()
                                                   .stream()
                                                   .map(RestrictionType::valueOfName)
                                                   .collect(Collectors.toSet());
    return new Customer(customerDto.getName(), restrictions);
  }

  public CustomerDto assembleDtoFromCustomer(Customer customer) {
    List<String> restrictions = customer.getRestrictions()
                                        .stream()
                                        .map(RestrictionType::toString)
                                        .collect(Collectors.toList());
    CustomerDto customerDto = new CustomerDto();
    customerDto.setName(customer.getName());
    customerDto.setRestrictions(restrictions);
    return customerDto;
  }
}
