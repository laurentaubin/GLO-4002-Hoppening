package ca.ulaval.glo4002.reservation.api.reservation.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;
import ca.ulaval.glo4002.reservation.api.reservation.dto.TableDto;

public class TableDtoBuilder {
  private final List<CustomerDto> customers;

  public TableDtoBuilder() {
    customers = new ArrayList<>();
  }

  public TableDtoBuilder withCustomer(CustomerDto customer) {
    customers.add(customer);
    return this;
  }

  public TableDtoBuilder withAnyCustomer() {
    customers.add(new CustomerDtoBuilder().build());
    return this;
  }

  public TableDtoBuilder withSpecifiedNumberOfCustomer(int numberOfCustomer) {
    for (int i = 0; i < numberOfCustomer; i++) {
      customers.add(new CustomerDtoBuilder().build());
    }
    return this;
  }

  public TableDto build() {
    TableDto tableDto = new TableDto();
    tableDto.setCustomers(customers);
    return tableDto;
  }
}
