package ca.ulaval.glo4002.reservation.api.reservation.dto;

import javax.validation.constraints.NotNull;

public class CountryDto {
  @NotNull
  private String code;

  @NotNull
  private String fullname;

  @NotNull
  private String currency;

  public void setCode(String code) {
    this.code = code;
  }

  public void setFullname(String name) {
    this.fullname = name;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
