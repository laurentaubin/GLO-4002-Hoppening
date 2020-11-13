package ca.ulaval.glo4002.reservation.api.reservation.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerApiDto;

public class CustomerDtoBuilder {
  private static final String A_NAME = "bob";

  private String name;
  private final List<String> restrictions;

  public CustomerDtoBuilder() {
    name = A_NAME;
    restrictions = new ArrayList<>();
  }

  public CustomerDtoBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public CustomerDtoBuilder withRestriction(String restriction) {
    restrictions.add(restriction);
    return this;
  }

  public CustomerApiDto build() {
    CustomerApiDto customerApiDto = new CustomerApiDto();
    customerApiDto.setName(name);
    customerApiDto.setRestrictions(restrictions);

    return customerApiDto;
  }
}
