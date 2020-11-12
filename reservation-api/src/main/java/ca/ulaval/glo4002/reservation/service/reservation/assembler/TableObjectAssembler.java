package ca.ulaval.glo4002.reservation.service.reservation.assembler;

import java.util.List;
import java.util.stream.Collectors;

import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;
import ca.ulaval.glo4002.reservation.service.reservation.CustomerObject;
import ca.ulaval.glo4002.reservation.service.reservation.TableObject;

public class TableObjectAssembler {
  private final CustomerObjectAssembler customerObjectAssembler;

  public TableObjectAssembler(CustomerObjectAssembler customerObjectAssembler) {
    this.customerObjectAssembler = customerObjectAssembler;
  }

  public TableObject assemble(TableDto tableDto) {
    List<CustomerObject> customerObjects = tableDto.getCustomers()
                                                   .stream()
                                                   .map(customerObjectAssembler::assemble)
                                                   .collect(Collectors.toList());
    return new TableObject(customerObjects);
  }
}
