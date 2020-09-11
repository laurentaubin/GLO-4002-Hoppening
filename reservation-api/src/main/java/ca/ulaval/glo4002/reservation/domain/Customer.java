package ca.ulaval.glo4002.reservation.domain;

import java.util.Set;

public class Customer {
  private final String name;
  private final Set<Restriction> restrictions;

  public Customer(String name, Set<Restriction> restrictions) {
    this.name = name;
    this.restrictions = restrictions;
  }

  public String getName() {
    return name;
  }

  public Set<Restriction> getRestrictions() {
    return restrictions;
  }
}
