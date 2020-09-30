package ca.ulaval.glo4002.reservation.domain.reservation;

public class Country {
  private final String code;
  private final String fullname;
  private final String currency;

  public Country(String code, String name, String currency) {
    this.code = code;
    this.fullname = name;
    this.currency = currency;
  }

  public String getCode() {
    return code;
  }

  public String getFullname() {
    return fullname;
  }

  public String getCurrency() {
    return currency;
  }
}
