package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TableDto {
  @Valid
  @NotNull
  private List<CustomerDto> customers;

  public void setCustomers(List<CustomerDto> customerDtos) {
    customers = customerDtos;
  }

  public List<CustomerDto> getCustomers() {
    return customers;
  }
}
