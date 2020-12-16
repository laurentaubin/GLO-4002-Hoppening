package ca.ulaval.glo4002.reservation.domain.builder;

import java.util.HashSet;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.reservation.Customer;
import ca.ulaval.glo4002.reservation.domain.reservation.RestrictionType;

public class CustomerBuilder {
  private static final String A_NAME = "Johnny";

  private String name;
  private final Set<RestrictionType> restrictions;

  public CustomerBuilder() {
    name = A_NAME;
    restrictions = new HashSet<>();
  }

  public CustomerBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public CustomerBuilder withRestriction(RestrictionType restriction) {
    restrictions.add(restriction);
    return this;
  }

  public CustomerBuilder withAnyRestriction() {
    restrictions.add(RestrictionType.VEGAN);
    return this;
  }

  public Customer build() {
    return new Customer(name, restrictions);
  }
}
