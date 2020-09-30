package ca.ulaval.glo4002.reservation.service.assembler;

import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

public class TableAssembler {
  private final CustomerAssembler customerAssembler;

  public TableAssembler(CustomerAssembler customerAssembler) {
    this.customerAssembler = customerAssembler;
  }

  public Table assembleFromTableDto(TableDto tableDto) {
    List<Customer> customers = tableDto.getCustomers()
                                       .stream()
                                       .map(customerAssembler::assembleFromCustomerDto)
                                       .collect(Collectors.toList());
    return new Table(customers);
  }
}
