package ca.ulaval.glo4002.reservation.service.reservation;

import java.util.List;

public class TableObject {
  private final List<CustomerObject> customerObjects;

  public TableObject(List<CustomerObject> customerObjects) {
    this.customerObjects = customerObjects;
  }

  public List<CustomerObject> getCustomerObjects() {
    return customerObjects;
  }
}
