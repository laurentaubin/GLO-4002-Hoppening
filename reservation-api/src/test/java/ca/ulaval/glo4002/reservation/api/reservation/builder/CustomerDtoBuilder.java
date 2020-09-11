package ca.ulaval.glo4002.reservation.api.reservation.builder;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CustomerDto;

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

  public CustomerDto build() {
    CustomerDto customerDto = new CustomerDto();
    customerDto.setName(name);
    customerDto.setRestrictions(restrictions);

    return customerDto;
  }
}
