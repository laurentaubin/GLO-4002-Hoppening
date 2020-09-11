package ca.ulaval.glo4002.reservation.domain.builder;

import java.util.HashSet;
import java.util.Set;

import ca.ulaval.glo4002.reservation.domain.Customer;
import ca.ulaval.glo4002.reservation.domain.Restriction;

public class CustomerBuilder {
  private static final String A_NAME = "Johnny";

  private String name;
  private final Set<Restriction> restrictions;

  public CustomerBuilder() {
    name = A_NAME;
    restrictions = new HashSet<>();
  }

  public CustomerBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public CustomerBuilder withRestriction(Restriction restriction) {
    restrictions.add(restriction);
    return this;
  }

  public CustomerBuilder withAnyRestriction() {
    restrictions.add(Restriction.VEGAN);
    return this;
  }

  public Customer build() {
    return new Customer(name, restrictions);
  }
}
