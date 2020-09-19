package ca.ulaval.glo4002.reservation.domain;

public enum RestrictionType {
  VEGETARIAN("vegetarian", 500),
  VEGAN("vegan", 1000),
  ALLERGIES("allergies", 0),
  ILLNESS("illness", 0);

  private final String hoppeningName;

  private final double fees;

  RestrictionType(String hoppeningName, double fees) {
    this.hoppeningName = hoppeningName;
    this.fees = fees;
  }

  @Override
  public String toString() {
    return hoppeningName;
  }

  public double getFees() {
    return fees;
  }

  public static RestrictionType valueOfName(String value) {
    for (RestrictionType restriction : RestrictionType.values()) {
      if (restriction.toString().equals(value)) {
        return restriction;
      }
    }
    throw new RuntimeException();
  }
}
