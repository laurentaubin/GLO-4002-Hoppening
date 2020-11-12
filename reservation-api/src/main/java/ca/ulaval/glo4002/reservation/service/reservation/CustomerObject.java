package ca.ulaval.glo4002.reservation.service.reservation;

import java.util.List;

public class CustomerObject {
  private final String name;
  private final List<String> restrictions;

  public CustomerObject(String name, List<String> restrictions) {
    this.name = name;
    this.restrictions = restrictions;
  }

  public String getName() {
    return name;
  }

  public List<String> getRestrictions() {
    return restrictions;
  }
}
