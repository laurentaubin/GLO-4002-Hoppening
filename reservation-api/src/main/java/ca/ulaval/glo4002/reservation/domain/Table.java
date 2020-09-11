package ca.ulaval.glo4002.reservation.domain;

import java.util.List;

public class Table {
  private final List<Customer> customers;

  public Table(List<Customer> customers) {
    this.customers = customers;
  }

  public List<Customer> getCustomers() {
    return customers;
  }
}
