package ca.ulaval.glo4002.reservation.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {

  private final long id;
  private final String vendorCode;
  private final LocalDateTime dinnerDate;
  private final List<Table> tables;

  public Reservation(long id, String vendorCode, LocalDateTime dinnerDate, List<Table> tables) {
    this.id = id;
    this.vendorCode = vendorCode;
    this.dinnerDate = dinnerDate;
    this.tables = tables;
  }

  public long getId() {
    return id;
  }

  public String getVendorCode() {
    return vendorCode;
  }

  public LocalDateTime getDinnerDate() {
    return dinnerDate;
  }

  public List<Table> getTables() {
    return tables;
  }
}
