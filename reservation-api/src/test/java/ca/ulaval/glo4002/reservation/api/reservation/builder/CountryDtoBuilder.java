package ca.ulaval.glo4002.reservation.api.reservation.builder;

import ca.ulaval.glo4002.reservation.api.reservation.dto.CountryDto;

public class CountryDtoBuilder {
  private static final String A_CODE = "CA";
  private static final String A_FULLNAME = "CANADA";
  private static final String A_CURRENCY = "CAD";

  private String code;
  private String fullname;
  private String currency;

  public CountryDtoBuilder() {
    this.code = A_CODE;
    this.fullname = A_FULLNAME;
    this.currency = A_CURRENCY;
  }

  public CountryDtoBuilder withCode(String code) {
    this.code = code;
    return this;
  }

  public CountryDtoBuilder withName(String name) {
    this.fullname = name;
    return this;
  }

  public CountryDtoBuilder withCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public CountryDto build() {
    CountryDto countryDto = new CountryDto();
    countryDto.setCode(this.code);
    countryDto.setFullname(this.fullname);
    countryDto.setCurrency(this.currency);

    return countryDto;
  }
}
