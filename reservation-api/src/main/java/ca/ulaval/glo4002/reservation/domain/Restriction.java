package ca.ulaval.glo4002.reservation.domain;

public enum Restriction {
  VEGETARIAN("vegetarian"), VEGAN("vegan"), ALLERGIES("allergies"), ILLNESS("illness");

  private final String hoppeningName;

  Restriction(String hoppeningName) {
    this.hoppeningName = hoppeningName;
  }

  @Override
  public String toString() {
    return hoppeningName;
  }

  public static Restriction valueOfHoppeningName(String value) {
    for (Restriction restriction : Restriction.values()) {
      if (restriction.toString().equals(value)) {
        return restriction;
      }
    }
    throw new RuntimeException();
  }
}
