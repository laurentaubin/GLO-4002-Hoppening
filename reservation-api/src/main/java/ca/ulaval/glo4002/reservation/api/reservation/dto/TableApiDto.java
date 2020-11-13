package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TableApiDto {
  @Valid
  @NotNull
  private List<CustomerApiDto> customers;

  public void setCustomers(List<CustomerApiDto> customerApiDtos) {
    customers = customerApiDtos;
  }

  public List<CustomerApiDto> getCustomers() {
    return customers;
  }
}
