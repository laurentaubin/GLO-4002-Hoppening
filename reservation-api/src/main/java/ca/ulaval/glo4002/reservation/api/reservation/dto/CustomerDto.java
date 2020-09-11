package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

public class CustomerDto {
  private String name;
  private List<String> restrictions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(List<String> restrictions) {
    this.restrictions = restrictions;
  }
}
