package ca.ulaval.glo4002.reservation.domain.reservation;

import java.util.Set;

public class CustomerRestriction {

  private final Set<RestrictionType> restrictions;

  public CustomerRestriction(Set<RestrictionType> restrictions) {
    this.restrictions = restrictions;
  }

  public Set<RestrictionType> getRestrictions() {
    return restrictions;
  }

  public double getAdditionalFees() {
    return restrictions.stream().mapToDouble(RestrictionType::getFees).sum();
  }
}
