package ca.ulaval.glo4002.reservation.domain.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.Table;

public class TableBuilder {
  private final List<Customer> customers;

  public TableBuilder() {
    customers = new ArrayList<>();
  }

  public TableBuilder withCustomer(Customer customer) {
    customers.add(customer);
    return this;
  }

  public TableBuilder withCustomers(List<Customer> customers) {
    this.customers.addAll(customers);
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
