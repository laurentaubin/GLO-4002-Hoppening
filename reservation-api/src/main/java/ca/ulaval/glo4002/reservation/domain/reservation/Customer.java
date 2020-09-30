package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.Set;

public class Customer {

  public static final double BASIC_CUSTOMER_FEES = 1000;
  private final String name;
  private final CustomerRestriction restriction;

  public Customer(String name, Set<RestrictionType> restrictions) {
    this.name = name;
    restriction = new CustomerRestriction(restrictions);
  }

  public String getName() {
    return name;
  }

  public Set<RestrictionType> getRestrictions() {
    return restriction.getRestrictions();
  }

  public double getCustomerFees() {
    return BASIC_CUSTOMER_FEES + restriction.getAdditionalFees();
  }
}
