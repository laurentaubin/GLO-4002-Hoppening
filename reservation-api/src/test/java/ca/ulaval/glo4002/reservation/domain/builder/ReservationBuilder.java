package ca.ulaval.glo4002.reservation.domain.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4002.reservation.domain.Reservation;
import ca.ulaval.glo4002.reservation.domain.Table;

public class ReservationBuilder {
  private static final int A_ID = 12345;
  private static final String A_VENDOR_CODE = "vendor code";
  private static final LocalDateTime A_DINNER_DATE = LocalDateTime.now();

  private long id;
  private String vendorCode;
  private LocalDateTime dinnerDate;
  private final List<Table> tables;

  public ReservationBuilder() {
    id = A_ID;
    vendorCode = A_VENDOR_CODE;
    dinnerDate = A_DINNER_DATE;
    tables = new ArrayList<>();
  }

  public ReservationBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public ReservationBuilder withVendorCode(String vendorCode) {
    this.vendorCode = vendorCode;
    return this;
  }

  public ReservationBuilder withDinnerDate(LocalDateTime dinnerDate) {
    this.dinnerDate = dinnerDate;
    return this;
  }

  public ReservationBuilder withTable(Table table) {
    tables.add(table);
    return this;
  }

  public ReservationBuilder withAnyTable() {
    Table table = new TableBuilder().withAnyCustomer().build();
    tables.add(table);
    return this;
  }

  public Reservation build() {
    return new Reservation(id, vendorCode, dinnerDate, tables);
  }
}
