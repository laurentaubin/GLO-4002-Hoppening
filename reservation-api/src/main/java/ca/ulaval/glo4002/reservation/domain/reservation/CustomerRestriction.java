package ca.ulaval.glo4002.reservation.domain.reservation;

import java.math.BigDecimal;
import java.util.Set;

public class CustomerRestriction {

  private final Set<RestrictionType> restrictions;

  public CustomerRestriction(Set<RestrictionType> restrictions) {
    this.restrictions = restrictions;
  }

  public Set<RestrictionType> getRestrictions() {
    return restrictions;
  }

  public BigDecimal getAdditionalFees() {
    BigDecimal additionalFees = BigDecimal.ZERO;
    for (RestrictionType restrictionType : restrictions) {
      additionalFees = additionalFees.add(restrictionType.getFees());
    }
    return additionalFees;
  }
}
