package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

public class TableDto {
  private List<CustomerDto> customers;

  public void setCustomers(List<CustomerDto> customerDtos) {
    customers = customerDtos;
  }

  public List<CustomerDto> getCustomers() {
    return customers;
  }
}
