package ca.ulaval.glo4002.reservation.domain.builder;

import ca.ulaval.glo4002.reservation.domain.Country;

public class CountryBuilder {
  private static final String A_CODE = "CA";
  private static final String A_NAME = "CANADA";
  private static final String A_CURRENCY = "CAD";

  private String code;
  private String name;
  private String currency;

  public CountryBuilder() {
    this.code = A_CODE;
    this.name = A_NAME;
    this.currency = A_CURRENCY;
  }

  public CountryBuilder withCode(String code) {
    this.code = code;
    return this;
  }

  public CountryBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public CountryBuilder withCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public Country build() {
    return new Country(this.code, this.name, this.currency);
  }
}
