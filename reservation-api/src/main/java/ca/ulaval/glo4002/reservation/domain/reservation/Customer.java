package ca.ulaval.glo4002.reservation.domain.reservation;

import java.math.BigDecimal;
import java.util.Set;

public class Customer {

  public static final BigDecimal BASIC_CUSTOMER_FEES = BigDecimal.valueOf(1000);
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

  public BigDecimal getCustomerFees() {
    return BASIC_CUSTOMER_FEES.add(restriction.getAdditionalFees());
  }
}
