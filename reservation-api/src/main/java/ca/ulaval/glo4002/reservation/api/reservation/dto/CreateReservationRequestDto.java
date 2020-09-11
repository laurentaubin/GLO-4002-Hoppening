package ca.ulaval.glo4002.reservation.api.reservation.dto;

import java.util.List;

public class CreateReservationRequestDto {
  private List<TableDto> tables;

  public void setTables(List<TableDto> tables) {
    this.tables = tables;
  }

  public List<TableDto> getTables() {
    return tables;
  }
}
