package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CreateReservationRequestDto {
  @NotNull
  private String vendorCode;

  @NotNull
  private String dinnerDate;

  @Valid
  @NotNull
  private List<TableDto> tables;

  public CreateReservationRequestDto() {
  }

  public String getVendorCode() {
    return vendorCode;
  }

  public void setVendorCode(String vendorCode) {
    this.vendorCode = vendorCode;
  }

  public String getDinnerDate() {
    return dinnerDate;
  }

  public void setDinnerDate(String dinnerDate) {
    this.dinnerDate = dinnerDate;
  }

  public void setTables(List<TableDto> tables) {
    this.tables = tables;
  }

  public List<TableDto> getTables() {
    return tables;
  }
}
