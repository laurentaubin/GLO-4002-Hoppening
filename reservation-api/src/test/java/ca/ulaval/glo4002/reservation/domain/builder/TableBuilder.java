package ca.ulaval.glo4002.reservation.domain.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.Table;

public class TableBuilder {
  private final List<Customer> customers;

  public TableBuilder() {
    customers = new ArrayList<>();
  }

  public TableBuilder withCustomer(Customer customer) {
    customers.add(customer);
    return this;
  }

  public TableBuilder withAnyCustomer() {
    Customer customer = new CustomerBuilder().withAnyRestriction().build();
    customers.add(customer);
    return this;
  }

  public Table build() {
    return new Table(customers);
  }
}
