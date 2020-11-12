package ca.ulaval.glo4002.reservation.service.reservation.assembler;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.service.reservation.CustomerObject;

public class CustomerObjectAssembler {
  public CustomerObject assemble(CustomerDto customerDto) {
    return new CustomerObject(customerDto.getName(), customerDto.getRestrictions());
  }
}
