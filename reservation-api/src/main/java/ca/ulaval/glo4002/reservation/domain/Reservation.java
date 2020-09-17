package ca.ulaval.glo4002.reservation.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {

  private final long id;
  private final String vendorCode;
  private final LocalDateTime dinnerDate;
  private final List<Table> tables;
  private final ReservationDetails reservationDetails;

  public Reservation(long id,
                     String vendorCode,
                     LocalDateTime dinnerDate,
                     List<Table> tables,
                     ReservationDetails reservationDetails)
  {
    this.id = id;
    this.vendorCode = vendorCode;
    this.dinnerDate = dinnerDate;
    this.tables = tables;
    this.reservationDetails = reservationDetails;
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

  public ReservationDetails getReservationDetails() {
    return reservationDetails;
  }
}
